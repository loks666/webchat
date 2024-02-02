package com.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/***
 * 页面转发控制器
 */
@Controller
public class PageController {

    /***
     * 聊天室
     * @return
     */
    @GetMapping({"/", "/index"})
    public String index() {
        return "client/chat";
    }

    /***
     * 登录鉴权路由
     * @return
     */
    @GetMapping("/auth")
    public String auth() {
        return "client/auth";
    }

    /***
     * 聊天室鉴权路由
     * @return
     */
    @GetMapping("/chat")
    public String chat() {
        return "client/chat";
    }


    /***
     * 聊天室鉴权路由
     * @return
     */
    @GetMapping("/admin")
    public String admin() {
        return "admin/console";
    }

    /**
     * 页面转发规则
     */
    @GetMapping({"/{packageName}/{pageName}"})
    public String page(@PathVariable String packageName, @PathVariable String pageName) {
        return packageName+"/"+pageName;
    }
}
