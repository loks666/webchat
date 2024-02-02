package com.webchat.domain.vo.response.mess;


import com.webchat.domain.vo.response.UserBaseResponseInfoVO;
import lombok.Data;

/***
 * 用户消息列表VO
 */
@Data
public class UserMessListResponseVO {

    /***
     * 当前用户
     */
    private UserBaseResponseInfoVO user;

    /***
     * 消息文本正文
     */
    private String lastMessage;

    /***
     * 消息发送时间
     */
    private Long time;

    /***
     * 未读消息数
     */
    private boolean unReadMess = false;
}
