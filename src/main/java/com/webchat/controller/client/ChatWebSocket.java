package com.webchat.controller.client;

import com.webchat.common.util.JsonUtil;
import com.webchat.common.util.SpringContextUtil;
import com.webchat.domain.vo.request.mess.ChatMessageRequestVO;
import com.webchat.domain.vo.response.mess.ChatMessageResponseVO;
import com.webchat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OnOpen 表示有浏览器链接过来的时候被调用
 * OnClose 表示浏览器发出关闭请求的时候被调用
 * OnMessage 表示浏览器发消息的时候被调用
 * OnError 表示有错误发生，比如网络断开了等等
 */
@Slf4j
@Component
@ServerEndpoint("/ws/chat/{userId}")
public class ChatWebSocket {

    /**
     * 在线人数
     */
    public static int onlineNumber = 0;

    /**
     * 以用户的Id作为key，WebSocket为对象保存起来
     */
    private static Map<String, ChatWebSocket> clients = new ConcurrentHashMap<String, ChatWebSocket>();

    /**
     * 会话
     */
    private Session session;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        this.onlineNumber++;
        this.userId = userId;
        this.session = session;
        clients.put(userId, this);
        log.info("onOpen success. sessionId:{}, userId:{}, online user count:{}", session.getId(), userId, onlineNumber);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("服务端发生了错误, error message:{}", error.getMessage());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        this.onlineNumber--;
        this.clients.remove(userId);
        log.info("onClose success！ online user count:P{}", this.onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            log.info("来自客户端消息：" + message + "客户端的id是：" + session.getId());
            UserService userService = SpringContextUtil.getBean(UserService.class);
            ChatMessageRequestVO chatMessage = JsonUtil.fromJson(message, ChatMessageRequestVO.class);
            ChatMessageResponseVO chatMessageResponse = new ChatMessageResponseVO();
            chatMessageResponse.setMessage(chatMessage.getMessage());
            chatMessageResponse.setSenderId(chatMessage.getSenderId());
            chatMessageResponse.setReceiverId(chatMessage.getReceiverId());
            chatMessageResponse.setSender(userService.getUserInfoByUserId(chatMessage.getSenderId()));
            chatMessageResponse.setReceiver(userService.getUserInfoByUserId(chatMessage.getReceiverId()));
            chatMessageResponse.setTime(new Date().getTime());
            sendMessageTo(JsonUtil.toJsonString(chatMessageResponse), chatMessage.getReceiverId());
        } catch (Exception e) {
            log.info("onMessage error. message:{}", message);
        }
    }

    public void sendMessageTo(String message, String receiver) throws IOException {
        for (ChatWebSocket chat : this.clients.values()) {
            if (chat.userId.equals(receiver)) {
                chat.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}