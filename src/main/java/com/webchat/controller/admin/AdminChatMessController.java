package com.webchat.controller.admin;

import com.webchat.common.bean.APIPageResponseBean;
import com.webchat.common.bean.APIResponseBean;
import com.webchat.common.bean.APIResponseBeanUtil;
import com.webchat.common.helper.SessionHelper;
import com.webchat.config.annotation.ValidateLogin;
import com.webchat.config.annotation.ValidatePermission;
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
public class AdminChatMessController {
    @Autowired
    private ChatMessService chatMessService;

    @ValidatePermission
    @GetMapping("/page")
    public APIPageResponseBean<List<ChatMessageResponseVO>> pageMessage(
            @RequestParam(value = "mess", required = false) String mess,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return chatMessService.pageMessage(mess, pageNo, pageSize);
    }
}
