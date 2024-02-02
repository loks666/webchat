package com.webchat.controller.client;

import com.webchat.common.bean.APIResponseBean;
import com.webchat.common.bean.APIResponseBeanUtil;
import com.webchat.common.util.AvatarUtil;
import com.webchat.domain.vo.request.UserLoginInfoRequestVO;
import com.webchat.domain.vo.request.UserRegistryInfoRequestVO;
import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import com.webchat.service.UserService;
import com.webchat.service.ValidCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


/**
 * 用户服务API
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ValidCodeService validCodeService;

    /**
     * 取当前登录用户信息
     * @return
     */
    @GetMapping("/getCurrentUserInfo")
    public APIResponseBean getCurrentUserInfo(@RequestParam(value = "nav", required = false, defaultValue = "false")
                                                          Boolean needNav) {
        UserBaseResponseInfoVO user = userService.getCurrentUserInfo();
        if (user == null) {
            return APIResponseBeanUtil.error(404, "no login");
        }
        return APIResponseBeanUtil.success(user);
    }

    /**
     * 用户登录
     * @param request
     * @return
     */
    @PostMapping("/login")
    public APIResponseBean login(@RequestBody UserLoginInfoRequestVO request) {
        Assert.isTrue(request != null, "登录参数为空！");
        String mobile = request.getMobile();
        String password = request.getPassword();
        Assert.isTrue(StringUtils.isNotBlank(mobile), "手机号为空！");
        Assert.isTrue(StringUtils.isNotBlank(password), "密码为空！");
        return APIResponseBeanUtil.success(userService.login(mobile, password));
    }

    @GetMapping("/logout")
    public APIResponseBean logout() {
        userService.logout();
        return APIResponseBeanUtil.success("OK");
    }

    /**
     * 用户注册
     * @param request
     * @return
     */
    @PostMapping("/registry")
    public APIResponseBean registry(@RequestBody UserRegistryInfoRequestVO request) {

        // 数字验证码核验
        validCodeService.validCode(request.getPicCheckCode());

        String photo = AvatarUtil.getRandomAvatar();
        userService.registry(photo, request.getUserName(), request.getMobile(), request.getPassword());
        return APIResponseBeanUtil.success("注册成功");
    }

    /**
     * 手机号检索
     * @param mobile
     * @return
     */
    @GetMapping("/search/{mobile}")
    public APIResponseBean<UserBaseResponseInfoVO> queryByMobile(@PathVariable String mobile) {

        return APIResponseBeanUtil.success(userService.queryByMobile(mobile));
    }
}
