package com.webchat.service;

import com.webchat.common.bean.APIPageResponseBean;
import com.webchat.common.enums.RedisKeyEnum;
import com.webchat.common.util.DateUtils;
import com.webchat.common.util.HtmlUtil;
import com.webchat.common.util.JsonUtil;
import com.webchat.common.util.ThreadPoolExecutorUtil;
import com.webchat.domain.vo.request.mess.ChatMessageRequestVO;
import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import com.webchat.domain.vo.response.mess.ChatMessageResponseVO;
import com.webchat.domain.vo.response.mess.UserMessListResponseVO;
import com.webchat.repository.dao.IChatMessDAO;
import com.webchat.repository.entity.ChatMessEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author by loks666 on GitHub
 * @webSite https://www.coderutil.com
 * @Date 2022/12/10 22:30
 * @description
 */
@Service
public class ChatMessService {

    @Autowired
    private IChatMessDAO chatMessDAO;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;

    public Long sendMess(ChatMessageRequestVO messVo) {
        ChatMessEntity mess = convert(messVo);
        Long messId = chatMessDAO.save(mess).getId();
        // 加入redis缓存
        ThreadPoolExecutorUtil.execute(() -> {
            // 加入聊天缓存
            this.addUserMessCache(mess);
            // 加入聊天列表
            this.addOrRefreshMessListCache(mess.getSender(), mess.getReceiver());
            this.addOrRefreshMessListCache(mess.getReceiver(), mess.getSender());
            // 未读消息+1
            addUnreadMessCountCache(messVo.getReceiverId(), messVo.getSenderId());
        });
        return messId;
    }

    private Long addUnreadMessCountCache(String currUserId, String chatUserId) {
        String unreadUserCacheCountKey = RedisKeyEnum.UN_READ_MESS_USER_SET_CACHE.getKey(currUserId);
        redisService.sadd(unreadUserCacheCountKey, chatUserId);
        String unreadCacheCountKey = RedisKeyEnum.UN_READ_MESS_COUNT_CACHE.getKey(currUserId, chatUserId);
        return redisService.increx(unreadCacheCountKey, RedisKeyEnum.UN_READ_MESS_COUNT_CACHE.getExpireTime());
    }

    private void clearUnreadMessCountCache(String currUserId, String chatUserId) {
        String unreadCacheCountKey = RedisKeyEnum.UN_READ_MESS_COUNT_CACHE.getKey(currUserId, chatUserId);
        redisService.set(unreadCacheCountKey, "0", RedisKeyEnum.UN_READ_MESS_COUNT_CACHE.getExpireTime());
        String unreadUserCacheCountKey = RedisKeyEnum.UN_READ_MESS_USER_SET_CACHE.getKey(currUserId);
        redisService.sremove(unreadUserCacheCountKey, chatUserId);
    }

    public Long getUnreadMessUserCountFromCache(String currUserId) {
        String unreadUserCacheCountKey = RedisKeyEnum.UN_READ_MESS_USER_SET_CACHE.getKey(currUserId);
        return redisService.ssize(unreadUserCacheCountKey);
    }

    private Set<String> getUnreadMessUserSetFromCache(String currUserId) {
        String unreadUserCacheCountKey = RedisKeyEnum.UN_READ_MESS_USER_SET_CACHE.getKey(currUserId);
        return redisService.smembers(unreadUserCacheCountKey);
    }

    /***
     * 查询私信用户列表
     * @param currUserId
     * @param lastTime
     * @param size
     * @return
     */
    public List<UserMessListResponseVO> getMessUserList(String selectUser, String currUserId, Long lastTime,
                                                        int size) {
        List<UserMessListResponseVO> list = new ArrayList<>();
        lastTime = lastTime == null ? DateUtils.getCurrentTimeMillis() : lastTime;
        String messUserKey = RedisKeyEnum.MESS_USER_LIST_KEY.getKey(currUserId);
        Set<String> userIdSet = redisService.zreverseRangeByScore(messUserKey, lastTime, 0, size);
        if (CollectionUtils.isEmpty(userIdSet)) {
            if (StringUtils.isNotBlank(selectUser)) {
                UserMessListResponseVO userMessListResponseVO = new UserMessListResponseVO();
                if (userMessListResponseVO != null) {
                    UserBaseResponseInfoVO user = userService.getUserBaseInfoByUserId(selectUser);
                    userMessListResponseVO.setUser(user);
                    userMessListResponseVO.setLastMessage("暂无聊天记录");
                    userMessListResponseVO.setTime(DateUtils.getCurrentTimeMillis());
                    list.add(userMessListResponseVO);
                }
            }
            return list;
        }
        Set<String> queryUserIds = new LinkedHashSet<>();
        if (StringUtils.isNotBlank(selectUser)) {
            queryUserIds.add(selectUser);
        }
        queryUserIds.addAll(userIdSet);
        List<UserBaseResponseInfoVO> users = userService.batchGetUserListInfoFromCache(new ArrayList<>(queryUserIds));
        Map<String, ChatMessageResponseVO> userMessMap = this.batchGetUserLastMess(currUserId, userIdSet);
        Set<String> unReadMessUsers = getUnreadMessUserSetFromCache(currUserId);
        return users.stream().map(user -> {
            UserMessListResponseVO userMessListResponse = new UserMessListResponseVO();
            userMessListResponse.setUser(user);
            ChatMessageResponseVO chatMessageResponse = userMessMap.get(user.getUserId());
            if (chatMessageResponse != null) {
                userMessListResponse.setLastMessage(chatMessageResponse.getPrintMessage());
                userMessListResponse.setTime(chatMessageResponse.getTime());
            } else {
                userMessListResponse.setLastMessage("暂无聊天记录");
                userMessListResponse.setTime(DateUtils.getCurrentTimeMillis());
            }
            userMessListResponse.setUnReadMess(unReadMessUsers.contains(user.getUserId()));
            return userMessListResponse;
        }).sorted(Comparator.comparing(UserMessListResponseVO::getTime).reversed()).collect(Collectors.toList());
    }

    /***
     * 查询两个人的聊天记录
     * @param currUserId
     * @param chatUserId
     * @param lastId
     * @param size
     * @return
     */
    public List<ChatMessageResponseVO> getChatMessListFromCache(String currUserId, String chatUserId, Long lastId,
                                                                int size) {

        // 查询后清理未读消息数
        clearUnreadMessCountCache(currUserId, chatUserId);

        lastId = lastId == null ? Long.MAX_VALUE : lastId;
        String cacheKey = getUserMessRedisKey(currUserId, chatUserId);
        Set<String> cacheSet = redisService.zreverseRangeByScore(cacheKey, lastId, 0, size);
        if (CollectionUtils.isEmpty(cacheSet)) {
            return Collections.emptyList();
        }
        return cacheSet.stream().map(cache -> {
            ChatMessageResponseVO messageResponse = getChatMessDetailFromCache(Long.valueOf(cache));
            messageResponse.setReceiver(userService.getUserInfoByUserId(messageResponse.getReceiverId()));
            messageResponse.setSender(userService.getUserInfoByUserId(messageResponse.getSenderId()));
            return messageResponse;
        }).sorted(Comparator.comparing(ChatMessageResponseVO::getMessId)).collect(Collectors.toList());
    }
    public Map<String, ChatMessageResponseVO> batchGetUserLastMess(String currUserId, Set<String> userIds) {
        Map<String, ChatMessageResponseVO> map = new HashMap<>();
        for (String userId : userIds) {
            String cacheKey = getUserMessRedisKey(currUserId, userId);
            Set<String> cacheSet =
                    redisService.zreverseRangeByScore(cacheKey, Long.MAX_VALUE, 0, 1);
            if (CollectionUtils.isNotEmpty(cacheSet)) {
                Long lastMessId = Long.valueOf(new ArrayList<>(cacheSet).get(0));
                ChatMessageResponseVO chatMessageResponseVO = getChatMessDetailFromCache(lastMessId);
                if (chatMessageResponseVO != null) {
                    map.put(userId, chatMessageResponseVO);
                }
            }
        }
        return map;
    }

    private void addOrRefreshMessListCache(String currUserId, String chatUserId) {
        String messUserKey = RedisKeyEnum.MESS_USER_LIST_KEY.getKey(currUserId);
        redisService.zadd(messUserKey, chatUserId, DateUtils.getCurrentTimeMillis());
    }

    private void addUserMessCache(ChatMessEntity mess) {
        long messId = mess.getId();
        /***
         * 刷新消息详情缓存
         */
        refreshMessCache(mess);
        /***
         * 加入用户消息列表
         */
        addUserMessCache(mess.getSender(), mess.getReceiver(), messId, messId);
        addUserMessCache(mess.getReceiver(), mess.getSender(), messId, messId);
    }

    private void addUserMessCache(String sender, String receiver, Long messId, Long score) {
        String cacheKey = getUserMessRedisKey(sender, receiver);
        redisService.zadd(cacheKey, messId.toString(), score);
    }

    /***
     * 刷新消息缓存
     * @param mess
     */
    private void refreshMessCache(ChatMessEntity mess) {
        String messKey = RedisKeyEnum.MESS_DETAIL_CACHE_KEY.getKey();
        redisService.hset(messKey, String.valueOf(mess.getId()), JsonUtil.toJsonString(mess),
                RedisKeyEnum.MESS_DETAIL_CACHE_KEY.getExpireTime());
    }

    private ChatMessageResponseVO getChatMessDetailFromCache(Long messId) {
        String messKey = RedisKeyEnum.MESS_DETAIL_CACHE_KEY.getKey();
        String messCache = redisService.hget(messKey, String.valueOf(messId));
        if (StringUtils.isBlank(messCache)) {
            return null;
        }
        ChatMessEntity userMessEntity = JsonUtil.fromJson(messCache, ChatMessEntity.class);
        ChatMessageResponseVO messageResponse = new ChatMessageResponseVO();
        messageResponse.setMessId(userMessEntity.getId());
        messageResponse.setMessage(userMessEntity.getMessage());
        messageResponse.setSenderId(userMessEntity.getSender());
        messageResponse.setReceiverId(userMessEntity.getReceiver());
        messageResponse.setImage(userMessEntity.getImage());
        messageResponse.setTime(userMessEntity.getSendDate().getTime());
        messageResponse.setIsRead(userMessEntity.getIsRead());
        return messageResponse;
    }

    public String getUserMessRedisKey(String sender, String receiver) {
        return RedisKeyEnum.USER_MESS_CACHE_KEY.getKey(sender, receiver);
    }

    public APIPageResponseBean<List<ChatMessageResponseVO>> pageMessage(String mess, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        Page<ChatMessEntity> chatMessEntityPage;
        if (StringUtils.isBlank(mess)) {
            chatMessEntityPage = chatMessDAO.findAll(pageable);
        } else {
            chatMessEntityPage = chatMessDAO.findAllByMessageLike("%"+mess+"%", pageable);
        }
        List<ChatMessageResponseVO> chatMessageResponseVOList = convertChatMessageResponseList(chatMessEntityPage.getContent());
        return APIPageResponseBean.success(pageNo, pageSize, chatMessEntityPage.getTotalElements(), chatMessageResponseVOList);
    }

    private List<ChatMessageResponseVO> convertChatMessageResponseList(List<ChatMessEntity> chatMessEntities) {
        if (CollectionUtils.isEmpty(chatMessEntities)) {
            return Collections.emptyList();
        }
        /**
         * 批量查询用户信息
         */
        Set<String> senderUserIds = chatMessEntities.stream().map(ChatMessEntity::getSender).collect(Collectors.toSet());
        Set<String> receiverUserIds = chatMessEntities.stream().map(ChatMessEntity::getReceiver).collect(Collectors.toSet());
        senderUserIds.addAll(receiverUserIds);
        List<String> batchUser = new ArrayList<>(senderUserIds);
        Map<String, UserBaseResponseInfoVO> userMap = userService.batchGetUserInfoFromCache(batchUser);
        return chatMessEntities.stream().map(chat -> {
            ChatMessageResponseVO chatMessageResponse = new ChatMessageResponseVO();
            chatMessageResponse.setTime(chat.getSendDate().getTime());
            chatMessageResponse.setSender(userMap.get(chat.getSender()));
            chatMessageResponse.setReceiver(userMap.get(chat.getReceiver()));
            chatMessageResponse.setMessage(chat.getMessage());
            return chatMessageResponse;
        }).collect(Collectors.toList());
    }

    private ChatMessEntity convert(ChatMessageRequestVO messVo) {
        ChatMessEntity mess = new ChatMessEntity();
        mess.setSender(messVo.getSenderId());
        mess.setReceiver(messVo.getReceiverId());
        mess.setMessage(this.handleSpecialHtmlTag(HtmlUtil.xssEscape(messVo.getMessage())));
        mess.setImage(messVo.getImage());
        mess.setSendDate(new Date());
        mess.setIsRead(false);
        return mess;
    }

    /***
     * 处理特殊字符
     * @param content
     * @return
     */
    private String handleSpecialHtmlTag(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        content = content.replaceAll("&lt;br&gt;", "<br>");
        content = content.replaceAll("&lt;b&gt;", "<b>");
        return content;
    }
}
