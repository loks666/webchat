package com.webchat.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 好友关系表
 */
@Data
@Entity
@Table(name = "web_chat_friend")
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "friend_id")
    private String friendId;

    @Column(name = "apply_date")
    private Date applyDate;

    @Column(name = "handle_date")
    private Date handleDate;

    @Column(name = "status")
    private Integer status;

    @Version
    @Column(name = "version")
    private Integer version;
}
