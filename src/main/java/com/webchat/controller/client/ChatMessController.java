package com.webchat.controller.client;

import com.webchat.common.bean.APIPageResponseBean;
import com.webchat.common.bean.APIResponseBean;
import com.webchat.common.bean.APIResponseBeanUtil;
import com.webchat.common.helper.SessionHelper;
import com.webchat.config.annotation.ValidateLogin;
import com.webchat.domain.vo.request.mess.ChatMessageRequestVO;
import com.webchat.domain.vo.response.mess.ChatMessageResponseVO;
import com.webchat.domain.vo.response.mess.UserMessListResponseVO;
import com.webchat.service.ChatMessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author by loks666 on GitHub
 * @webSite https://www.coderutil.com
 * @Date 2022/12/10 22:29
 * @description
 */
@RestController
@RequestMapping("/api/chat/mess")
public class ChatMessController {

    @Autowired
    private ChatMessService chatMessService;

    /***
     * 发送消息
     * @return
     */
    @ValidateLogin
    @PostMapping("/send")
    public APIResponseBean sendMessage(@RequestBody ChatMessageRequestVO message) {
        String currUserId = SessionHelper.getCurrentUserId();
        message.setSenderId(currUserId);
        return APIResponseBeanUtil.success(chatMessService.sendMess(message));
    }

    /***
     * 获取未读消息人数
     * @return
     */
    @ValidateLogin
    @GetMapping("/unReadUserCount")
    public APIResponseBean<Long> getUnReadUserCount() {
        String currUserId = SessionHelper.getCurrentUserId();
        return APIResponseBeanUtil.success(chatMessService.getUnreadMessUserCountFromCache(currUserId));
    }

    /***
     * 查询跟用户聊天记录
     * @return
     */
    @ValidateLogin
    @GetMapping("/query/{chatUserId}/{size}")
    public APIResponseBean<List<ChatMessageResponseVO>> query(@PathVariable String chatUserId,
                                                              @PathVariable Integer size,
                                                              @RequestParam(value = "lastId", required = false) Long lastId) {
        String currUserId = SessionHelper.getCurrentUserId();
        return APIResponseBeanUtil.success(
                chatMessService.getChatMessListFromCache(currUserId, chatUserId, lastId, size));
    }

    /***
     * 查询跟用户聊天记录
     * @return
     */
    @ValidateLogin
    @GetMapping("/user.list")
    public APIResponseBean<List<UserMessListResponseVO>> userList(
            @RequestParam(value = "selectUser", required = false) String selectUser) {
        String currUserId = SessionHelper.getCurrentUserId();
        return APIResponseBeanUtil.success(chatMessService.getMessUserList(selectUser, currUserId, null, 200));
    }
}
