package com.webchat.controller.client;

import com.webchat.common.bean.APIResponseBean;
import com.webchat.common.bean.APIResponseBeanUtil;
import com.webchat.common.helper.SessionHelper;
import com.webchat.config.annotation.ValidateLogin;
import com.webchat.domain.vo.response.FriendApplyUserVO;
import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import com.webchat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author by loks666 on GitHub
 * @webSite https://www.coderutil.com
 * @Date 2022/12/10 15:39
 * @description
 */
@RestController
@RequestMapping("/api/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @ValidateLogin
    @PostMapping("/applyAdd/{friendId}")
    public APIResponseBean<Boolean> applyAddFriend(@PathVariable String friendId) {
        String userId = SessionHelper.getCurrentUserId();
        return APIResponseBeanUtil.success(friendService.applyAddFriend(userId, friendId));
    }

    @ValidateLogin
    @PostMapping("/applyPass/{id}")
    public APIResponseBean<Boolean> applyPass(@PathVariable Long id) {
        String userId = SessionHelper.getCurrentUserId();
        friendService.applyPass(userId, id);
        return APIResponseBeanUtil.success(true);
    }

    @ValidateLogin
    @PostMapping("/applyRefuse/{id}")
    public APIResponseBean<Boolean> applyRefuse(@PathVariable Long id) {
        String userId = SessionHelper.getCurrentUserId();
        friendService.applyRefuse(userId, id);
        return APIResponseBeanUtil.success(true);
    }

    @ValidateLogin
    @GetMapping("/countUnHandleApply")
    public APIResponseBean<Long> countUnHandleApply() {
        String userId = SessionHelper.getCurrentUserId();
        return APIResponseBeanUtil.success(friendService.countUnHandleApply(userId));
    }

    @ValidateLogin
    @GetMapping("/listUnHandleApplyUsers")
    public APIResponseBean<List<FriendApplyUserVO>> listUnHandleApplyUsers() {
        String userId = SessionHelper.getCurrentUserId();
        return APIResponseBeanUtil.success(friendService.listUnHandleApplyUsers(userId));
    }

    @ValidateLogin
    @GetMapping("/listFriendUsers")
    public APIResponseBean<List<FriendApplyUserVO>> listFriendUsers(@RequestParam(value = "userName", required = false) String userName) {
        String userId = SessionHelper.getCurrentUserId();
        return APIResponseBeanUtil.success(friendService.listFriendUsers(userId, userName));
    }
}
