package com.webchat.controller.admin;

import com.webchat.common.bean.APIPageResponseBean;
import com.webchat.common.bean.APIResponseBean;
import com.webchat.common.bean.APIResponseBeanUtil;
import com.webchat.common.enums.RoleCodeEnum;
import com.webchat.common.util.AvatarUtil;
import com.webchat.config.annotation.ValidatePermission;
import com.webchat.domain.vo.request.UserLoginInfoRequestVO;
import com.webchat.domain.vo.request.UserRegistryInfoRequestVO;
import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import com.webchat.domain.vo.response.UserSafeResponseInfoVO;
import com.webchat.service.UserService;
import com.webchat.service.ValidCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: by loks666 on GitHub
 * @Date: 11.3.22 1:12 上午
 * 用户服务API
 */
@RestController
@RequestMapping("/api/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    /***
     * 查询当前管理员权限，与上面getCurrentUserInfo内容一样，不同在于增加了 @ValidatePermission
     * @return
     */
    @ValidatePermission
    @GetMapping("/getCurrentAdminUserInfo")
    public APIResponseBean getCurrentAdminUserInfo() {
        UserBaseResponseInfoVO user = userService.getCurrentUserInfo();
        if (user != null) {
            return APIResponseBeanUtil.success(user);
        }
        return APIResponseBeanUtil.error(404, "no login");
    }

    /***
     * 加入黑名单角色
     * @param userId
     * @return
     */
    @ValidatePermission(role = RoleCodeEnum.ADMIN)
    @PostMapping("/addBlackListRole/{userId}")
    public APIResponseBean addBlackList(@PathVariable String userId) {
        userService.updateUserRole(userId, RoleCodeEnum.BLACK.getCode());
        return APIResponseBeanUtil.success();
    }

    /***
     * 升级管理员角色
     * @param userId
     * @return
     */
    @ValidatePermission(role = RoleCodeEnum.ADMIN)
    @PostMapping("/upgradeAdminRole/{userId}")
    public APIResponseBean upgradeAdminRole(@PathVariable String userId) {
        userService.updateUserRole(userId, RoleCodeEnum.ADMIN.getCode());
        return APIResponseBeanUtil.success();
    }

    /***
     * 设置为普通用户角色
     * @param userId
     * @return
     */
    @ValidatePermission(role = RoleCodeEnum.ADMIN)
    @PostMapping("/setNormalUserRole/{userId}")
    public APIResponseBean cancelBlackListRole(@PathVariable String userId) {
        userService.updateUserRole(userId, RoleCodeEnum.USER.getCode());
        return APIResponseBeanUtil.success();
    }

    @ValidatePermission(role = RoleCodeEnum.ADMIN)
    @GetMapping("/getUserList")
    public APIPageResponseBean<List<UserSafeResponseInfoVO>> getUserList(
            @RequestParam(value = "roleCode", required = false) Integer roleCode,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {

        return userService.getUserList(roleCode, pageNo, pageSize);
    }
}
