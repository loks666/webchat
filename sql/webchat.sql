-- 创建数据库 [MySQL]
CREATE DATABASE `webchat` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_bin';
use webchat;

-- 用户信息主表
CREATE TABLE webchat.`web_chat_user`
(
    `ID`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `USER_ID`     char(60)        NOT NULL COMMENT '用户ID',
    `USER_NAME`   char(30)        NOT NULL COMMENT '用户名',
    `PHOTO`       varchar(400)    NOT NULL COMMENT '用户头像',
    `MOBILE`      char(20)        NOT NULL COMMENT '手机号',
    `PASSWORD`    char(100)       NOT NULL COMMENT '密码',
    `STATUS`      INT             NOT NULL DEFAULT 1 COMMENT '用户状态状态',
    `ROLE_CODE`   INT             NOT NULL DEFAULT 1 COMMENT '角色',
    `CREATE_BY`   char(100)                DEFAULT NULL COMMENT '创建人',
    `CREATE_DATE` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UPDATE_BY`   char(100)                DEFAULT NULL COMMENT '更新人',
    `UPDATE_DATE` datetime                 DEFAULT NULL COMMENT '更新时间',
    `VERSION`     int                      DEFAULT '0' COMMENT '版本',
    PRIMARY KEY (`ID`),
    KEY `INDEX_USER_ID` (`USER_ID`),
    KEY `INDEX_MOBILE_PASSWORD` (`MOBILE`, `PASSWORD`),
    KEY `INDEX_STATUS` (`STATUS`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息主表';

-- 初始化默认管理员账号
INSERT INTO `webchat`.`web_chat_user` (`ID`, `USER_ID`, `USER_NAME`, `PHOTO`, `MOBILE`, `PASSWORD`, `STATUS`,
                                       `ROLE_CODE`,
                                       `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `VERSION`)
VALUES (1, 'U_770cce9f632543588b4e8aa6ec43e6a2', '管理员',
        'https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/13.png',
        'admin', '7bd0482b7e4eaea094c274dcb2b18511', 1, 2, 'U_770cce9f632543588b4e8aa6ec43e6a2',
        '2022-03-12 05:55:26', NULL, '2022-03-22 10:28:38', 1);
INSERT INTO `webchat`.`web_chat_user` (`ID`, `USER_ID`, `USER_NAME`, `PHOTO`, `MOBILE`, `PASSWORD`, `STATUS`,
                                       `ROLE_CODE`, `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `VERSION`)
VALUES (2, 'U_4276ed386d264ac6b60ee20767eda137', 'user1', 'https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/27.png',
        '16888888888', '7bd0482b7e4eaea094c274dcb2b18511', 1, 1, 'U_4276ed386d264ac6b60ee20767eda137',
        '2024-02-02 23:00:23', NULL, NULL, 0);
INSERT INTO `webchat`.`web_chat_user` (`ID`, `USER_ID`, `USER_NAME`, `PHOTO`, `MOBILE`, `PASSWORD`, `STATUS`,
                                       `ROLE_CODE`, `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `VERSION`)
VALUES (3, 'U_4628f2c1db4a4fa9bf835445fdc9934f', 'user2', 'https://coderutil.oss-cn-beijing.aliyuncs.com/avatar/11.png',
        '13888888888', '7bd0482b7e4eaea094c274dcb2b18511', 1, 1, 'U_4628f2c1db4a4fa9bf835445fdc9934f',
        '2024-02-02 23:58:31', NULL, NULL, 0);


-- 好友关系表
CREATE TABLE webchat.`web_chat_friend`
(
    `ID`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `USER_ID`     char(60)        NOT NULL COMMENT '用户ID',
    `FRIEND_ID`   char(60)        NOT NULL COMMENT '好友ID',
    `STATUS`      int(11)  DEFAULT '0' COMMENT '好友状态',
    `APPLY_DATE`  datetime DEFAULT NULL COMMENT '申请时间',
    `HANDLE_DATE` datetime DEFAULT NULL COMMENT '处理时间',
    `VERSION`     int      DEFAULT '0' COMMENT '版本',
    PRIMARY KEY (`ID`),
    KEY `INDEX_USER_ID` (`USER_ID`),
    KEY `INDEX_FRIEND_ID` (`FRIEND_ID`),
    KEY `INDEX_STATUS` (`STATUS`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='好友关系表';

CREATE TABLE webchat.`web_chat_mess`
(
    `ID`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `sender`      char(100)       NOT NULL COMMENT '发送人',
    `receiver`    char(100)       NOT NULL COMMENT '接收人',
    `message`     varchar(1000)            DEFAULT NULL COMMENT '消息内容',
    `image`       varchar(300)             DEFAULT NULL COMMENT '图片',
    `IS_READ`     tinyint(1)               DEFAULT 0 COMMENT '是否已读',
    `SEND_DATE`   datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息时间',
    `UPDATE_DATE` datetime                 DEFAULT NULL COMMENT '更新时间',
    `VERSION`     int                      DEFAULT '0' COMMENT '版本',
    PRIMARY KEY (`ID`),
    KEY `INDEX_SENDER` (`sender`),
    KEY `INDEX_RECEIVER` (`receiver`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='用户私信表';