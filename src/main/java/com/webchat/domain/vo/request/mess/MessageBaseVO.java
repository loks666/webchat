package com.webchat.domain.vo.request.mess;

import lombok.Data;

/**
 * @Author: by loks666 on GitHub
 * @Date: 10.9.22 11:38 下午
 */
@Data
public class MessageBaseVO {

    /***
     * 当前用户ID
     */
    private String senderId;

    /***
     * 聊天对象用户ID
     */
    private String receiverId;

    /***
     * 消息文本正文
     */
    private String message;
}
