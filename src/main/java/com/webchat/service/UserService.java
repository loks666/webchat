package com.webchat.service;

import com.webchat.common.bean.APIPageResponseBean;
import com.webchat.common.constants.CookieConstants;
import com.webchat.common.constants.WebConstant;
import com.webchat.common.enums.APIErrorCommonEnum;
import com.webchat.common.enums.RedisKeyEnum;
import com.webchat.common.enums.RoleCodeEnum;
import com.webchat.common.enums.UserStatusEnum;
import com.webchat.common.exception.BusinessException;
import com.webchat.common.helper.SessionHelper;
import com.webchat.common.util.*;
import com.webchat.domain.convert.UserEntryConvert;
import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import com.webchat.domain.vo.response.UserSafeResponseInfoVO;
import com.webchat.repository.dao.IUserDAO;
import com.webchat.repository.entity.UserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private RedisService redisService;

    /***
     * USER ID 前缀
     */
    private static final String USER_ID_PREFIX = "U";

    /**
     * 用户登录 - 登录成功返回用户基本信息
     *
     * @param mobile
     * @param password
     * @return
     */
    public UserBaseResponseInfoVO login(String mobile, String password) {
        if (!hasUserInfo(mobile)) {
            throw new BusinessException(APIErrorCommonEnum.USER_NOT_FOUND);
        }
        System.out.println(md5Pwd(password));
        UserEntity userEntity = userDAO.findByMobileAndPassword(mobile, md5Pwd(password));
        if (userEntity == null) {
            throw new BusinessException("密码验证失败");
        }
        if (userEntity.getRoleCode().equals(RoleCodeEnum.BLACK.getCode())) {
            throw new BusinessException("账号异常！联系管理员解封");
        }
        UserBaseResponseInfoVO user = userLogin(userEntity);
        return user;
    }

    public static void main(String[] args) {
        System.out.println(md5Pwd1("123456"));
    }



    private UserBaseResponseInfoVO userLogin(UserEntity userEntity) {
        // 创建用户登录会话数据
        this.createLoginSession(userEntity.getUserId());
        UserBaseResponseInfoVO user = UserEntryConvert.convertBaseVo(userEntity);
        return user;
    }

    /**
     * 用户注册 - 注册成功自动登录
     *
     * @param photo
     * @param userName
     * @param mobile
     * @param password
     * @return
     */
    @Transactional
    public boolean registry(String photo, String userName, String mobile, String password) {

        /***
         * 防快速点击
         */
        String key = RedisKeyEnum.USER_REGISTRY_LIMIT.getKey(mobile);
        boolean lockResult = redisService.installDistributedLock(key, WebConstant.CACHE_NONE, RedisKeyEnum.USER_REGISTRY_LIMIT.getExpireTime());
        Assert.isTrue(lockResult, "注册中请稍等！请勿频繁点击");

        if (hasUserInfo(mobile)) {
            throw new BusinessException("用户已存在");
        }
        if (!Pattern.matches("^[0-9a-zA-Z_]{1,}$", mobile)) {
            throw new BusinessException("账号格式不合法");
        }
        if (StringUtils.isBlank(userName) || userName.length() < 2 || userName.length() > 12) {
            throw new BusinessException("用户名不合法：2~12位字符");
        }
        if (userNameIsExist(userName)) {
            throw new BusinessException("用户名已经被占用，换一个吧");
        }
        // 注册
        this.registryUser2DB(userName, photo, mobile, password);
        return true;
    }

    public UserBaseResponseInfoVO queryByMobile(String mobile) {
        UserEntity userEntity = userDAO.findByMobile(mobile);
        return UserEntryConvert.convertBaseVo(userEntity);
    }

    public APIPageResponseBean<List<UserSafeResponseInfoVO>> getUserList(Integer roleCode, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<UserEntity> userEntities;
        if (roleCode == null) {
            userEntities = userDAO.findAll(pageable);
        } else {
            userEntities = userDAO.findAllByRoleCode(roleCode, pageable);
        }
        List<UserSafeResponseInfoVO> userSafeResponseInfoVOS = new ArrayList<>();
        if (userEntities != null && CollectionUtils.isNotEmpty(userEntities.getContent())) {
            userEntities.getContent().stream().forEach(u -> {
                UserSafeResponseInfoVO userSafeResponseInfoVO = UserEntryConvert.convertSafeVo(u);
                userSafeResponseInfoVOS.add(userSafeResponseInfoVO);
            });
        }
        return APIPageResponseBean.success(pageNo, pageSize, userEntities.getTotalElements(), userSafeResponseInfoVOS);
    }

    /**
     * 退出登录
     */
    public void logout() {
        String userId = getLoginCurrentUserId();
        // 清理登录Session
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (CookieConstants.C_U_USER_COOKIE_KEY.equals(cookie.getName())) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        // 清理缓存
        if (StringUtils.isNotBlank(userId)) {
            String sessionKey = RedisKeyEnum.USER_SESSION_PREFIX.getKey(MD5Utils.md5(userId));
            redisService.remove(sessionKey);
        }
    }

    private void registryUser2DB(String userName, String photo, String mobile, String password) {
        UserEntity userEntity = new UserEntity();
        String userId = IDGenerateUtil.createId(USER_ID_PREFIX);
        userEntity.setUserId(userId);
        userEntity.setUserName(StringUtil.handleSpecialHtmlTag(userName));
        userEntity.setPhoto(photo);
        userEntity.setMobile(mobile);
        userEntity.setStatus(UserStatusEnum.ENABLE.getStatus());
        userEntity.setPassword(md5Pwd(password));
        userEntity.setRoleCode(RoleCodeEnum.USER.getCode());
        userEntity.setCreateBy(userId);
        // 注册用户
        final String uid = userDAO.save(userEntity).getUserId();
        // 注册成功，事务结束后刷新用户缓存信息
        TransactionSyncManagerUtil.registerSynchronization(() -> {
            // 刷新用户缓存
            this.refreshAndGetUserEntityFromCache(uid);
        });
    }

    /**
     * 判断用户是否存在
     *
     * @param mobile
     * @return
     */
    private boolean hasUserInfo(String mobile) {
        return userDAO.findByMobile(mobile) != null;
    }

    private boolean userNameIsExist(String userName) {
        return userDAO.findByUserName(userName) != null;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public UserBaseResponseInfoVO getCurrentUserInfo() {

        return getLoginUserBaseInfoFromCache();
    }

    public UserBaseResponseInfoVO getLoginUserBaseInfoFromCache() {
        String userId = this.getLoginCurrentUserId();
        return getUserBaseInfoByUserIdFromCache(userId);
    }

    public UserBaseResponseInfoVO getUserInfoByUserId(String userId) {
        return getUserBaseInfoByUserId(userId);
    }

    public UserBaseResponseInfoVO getUserBaseInfoByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        return getUserBaseInfoByUserIdFromCache(userId);
    }

    private UserBaseResponseInfoVO getUserBaseInfoByUserIdFromCache(String userId) {
        if (userId == null) {
            return null;
        }
        UserEntity entity = this.refreshAndGetUserEntityFromCache(userId);
        UserBaseResponseInfoVO userBaseResponseInfoVO = UserEntryConvert.convertBaseVo(entity);
        if (userBaseResponseInfoVO == null) {
            return null;
        }
        return userBaseResponseInfoVO;
    }

    /***
     * 强制刷新用户信息缓存
     * @param userId
     */
    private void refreshUserDetailCache(String userId) {
        this.refreshAndGetUserEntityFromCache(userId, true);
    }

    private UserEntity refreshAndGetUserEntityFromCache(String userId, boolean... refreshCache) {
        String key = RedisKeyEnum.USER_INFO_CACHE.getKey(userId);
        String val = redisService.get(key);
        boolean refresh = refreshCache != null && refreshCache.length > 0 && refreshCache[0];
        if (!refresh && StringUtils.isNotBlank(val)) {
            return JsonUtil.fromJson(val, UserEntity.class);
        }
        UserEntity userEntity = userDAO.findByUserId(userId);
        if (userEntity != null) {
            redisService.set(key, JsonUtil.toJsonString(userEntity), RedisKeyEnum.USER_INFO_CACHE.getExpireTime());
        }
        return userEntity;
    }

    /***
     * 取当前用户ID，且未登录时直接抛出未登录异常
     * @return
     */
    public String getLoginCurrentUserIdOrElseThrowUnLoginExp() {
        String userId = getLoginCurrentUserId();
        if (StringUtils.isNotBlank(userId)) {
            return userId;
        }
        throw new BusinessException(APIErrorCommonEnum.USER_UN_LOGIN);
    }

    /***
     * 更新用户角色
     * @param userId
     * @param roleCode
     */
    public void updateUserRole(String userId, Integer roleCode) {
        UserEntity userEntity = userDAO.findByUserId(userId);
        Assert.isTrue(userEntity != null, "更新失败，用户不存在!");
        userEntity.setRoleCode(roleCode);
        userDAO.save(userEntity);
        /***
         * 刷新用户信息缓存
         */
        this.refreshUserDetailCache(userId);
    }

    /**
     * 获取当前登录的用户ID
     *
     * @return
     */
    public String getLoginCurrentUserId() {
        return SessionHelper.getCurrentUserId();
    }

    private String md5Pwd(String password) {
        return MD5Utils.md5(password.concat(WebConstant.MD5_SALT));
    }

    private static String md5Pwd1(String password) {
        return MD5Utils.md5(password.concat(WebConstant.MD5_SALT));
    }

    /**
     * 创建用户登录会话数据
     *
     * @param userId
     */
    private void createLoginSession(String userId) {
        String sessionId = MD5Utils.md5(userId);
        // UserId缓存
        String sessionKey = RedisKeyEnum.USER_SESSION_PREFIX.getKey(sessionId);
        redisService.set(sessionKey, userId, RedisKeyEnum.USER_SESSION_PREFIX.getExpireTime());
        // 种Cookie
        Cookie cookie = new Cookie(CookieConstants.C_U_USER_COOKIE_KEY, sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(CookieConstants.COOKIE_OUT_TIME);
        response.addCookie(cookie);
    }

    public String getUserIdBySessionId(String sessionId) {
        String sessionKey = RedisKeyEnum.USER_SESSION_PREFIX.getKey(sessionId);
        return redisService.get(sessionKey);
    }

    public Map<String, UserBaseResponseInfoVO> batchGetUserInfoFromCache(List<String> userIdList) {
        Map<String, UserBaseResponseInfoVO> userBaseResponseInfoVOMap = new HashMap<>();
        if (CollectionUtils.isEmpty(userIdList)) {
            return userBaseResponseInfoVOMap;
        }
        List<String> cacheKeys = userIdList.stream().map(k ->
                RedisKeyEnum.USER_INFO_CACHE.getKey(String.valueOf(k))).collect(Collectors.toList());
        Map<String, String> multiResultMap = redisService.mgetAndParseMap(cacheKeys);
        for (int i = 0; i < userIdList.size(); i++) {
            String userId = userIdList.get(i);
            String cacheKey = cacheKeys.get(i);
            String cache = multiResultMap.get(cacheKey);
            UserBaseResponseInfoVO userBaseResponseInfoVO;
            if (StringUtils.isNotBlank(cache)) {
                userBaseResponseInfoVO = JsonUtil.fromJson(cache, UserBaseResponseInfoVO.class);
            } else {
                userBaseResponseInfoVO = getUserBaseInfoByUserId(userId);
            }
            userBaseResponseInfoVOMap.put(userId, userBaseResponseInfoVO);
        }
        return userBaseResponseInfoVOMap;
    }

    public List<UserBaseResponseInfoVO> batchGetUserListInfoFromCache(List<String> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Collections.emptyList();
        }
        String currUser = SessionHelper.getCurrentUserId();
        List<String> cacheKeys = userIdList.stream().map(k ->
                RedisKeyEnum.USER_INFO_CACHE.getKey(String.valueOf(k))).collect(Collectors.toList());
        Map<String, String> multiResultMap = redisService.mgetAndParseMap(cacheKeys);
        List<UserBaseResponseInfoVO> userBaseResponseInfoVOList = new ArrayList<>();
        for (int i = 0; i < userIdList.size(); i++) {
            String userId = userIdList.get(i);
            String cacheKey = cacheKeys.get(i);
            String cache = multiResultMap.get(cacheKey);
            UserBaseResponseInfoVO userBaseResponseInfoVO;
            if (StringUtils.isNotBlank(cache)) {
                userBaseResponseInfoVO = JsonUtil.fromJson(cache, UserBaseResponseInfoVO.class);
            } else {
                userBaseResponseInfoVO = getUserBaseInfoByUserId(userId);
            }
            userBaseResponseInfoVOList.add(userBaseResponseInfoVO);
        }
        return userBaseResponseInfoVOList;
    }

}
