package com.webchat.domain.vo.response.mess;


import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ChatMessageResponseVO {

    /***
     * 消息ID
     */
    private Long messId;

    /***
     * 当前用户
     */
    private String senderId;
    private UserBaseResponseInfoVO sender;

    /***
     * 聊天对象
     */
    private String receiverId;
    private UserBaseResponseInfoVO receiver;

    /***
     * 消息文本正文
     */
    private String message;

    /***
     * 消息图片
     */
    private String image;

    /***
     * 消息发送时间
     */
    private Long time;

    /***
     * 消息状态
     */
    private Boolean isRead;


    public String getPrintMessage() {
        if (StringUtils.isNotBlank(message)) {
            return message;
        }
        if (StringUtils.isNotBlank(image)) {
            return "[图片]";
        }
        return "无消息";
    }
}
