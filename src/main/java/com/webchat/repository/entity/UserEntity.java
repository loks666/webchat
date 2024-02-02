package com.webchat.repository.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "web_chat_user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * 用户UUID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 昵称
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 头像
     */
    @Column(name = "photo")
    private String photo;

    /**
     * 角色CODE
     */
    @Column(name = "role_code")
    private Integer roleCode;

    /**
     * 用户状态状态
     */
    @Column(name = "status")
    private Integer status;
}
