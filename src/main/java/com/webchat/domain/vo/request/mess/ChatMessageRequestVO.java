package com.webchat.domain.vo.request.mess;

import lombok.Data;


/***
 * 聊天消息发送VO
 */
@Data
public class ChatMessageRequestVO extends MessageBaseVO {

    /***
     * 消息图片
     */
    private String image;
}
