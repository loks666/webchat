package com.webchat.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "web_chat_mess")
public class ChatMessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /***
     * 发送人
     */
    @Column(name = "sender")
    private String sender;

    /***
     * 接收人
     */
    @Column(name = "receiver")
    private String receiver;

    /***
     * 正文
     */
    @Column(name = "message")
    private String message;

    /***
     * 图片
     */
    @Column(name = "image")
    private String image;

    /***
     * 状态
     */
    @Column(name = "is_read")
    private Boolean isRead;

    /***
     * 发送时间
     */
    @Column(name = "send_date")
    private Date sendDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Version
    @Column(name = "version")
    private Integer version;

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        if (this.sendDate == null) {
            this.sendDate = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = new Date();
    }
}
