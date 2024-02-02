package com.webchat.common.enums;

import com.webchat.common.constants.WebConstant;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum RedisKeyEnum {

    /***
     * 注册限流，5S内一次
     */
    USER_REGISTRY_LIMIT("USER_REGISTRY_LIMIT", 5L),

    /**
     * 用户信息缓存
     */
    USER_INFO_CACHE("USER_INFO_CACHE", -1L),

    /***
     * 私信用户列表
     */
    MESS_USER_LIST_KEY("MESS_USER_LIST_KEY", -1L),

    /***
     * 用户私信消息
     */
    USER_MESS_CACHE_KEY("USER_MESS_CACHE_KEY", -1L),

    /***
     * 私信消息缓存
     */
    MESS_DETAIL_CACHE_KEY("MESS_DETAIL_CACHE_KEY", -1L),

    /**
     * 未读私信数量
     */
    UN_READ_MESS_COUNT_CACHE("UN_READ_MESS_COUNT_CACHE", -1L),

    /**
     * 未读私信人数量
     */
    UN_READ_MESS_USER_SET_CACHE("UN_READ_MESS_USER_SET_CACHE", -1L),

    /***
     * 用户登录Session PREFIX
     */
    USER_SESSION_PREFIX("USER_SESSION_PREFIX", 3 * 24 * 60 * 60L);

    RedisKeyEnum(String key, Long expireTime) {
        this.key = key;
        this.expireTime = expireTime;
    }

    private String key;

    /**
     * 有效时间,单位秒
     */
    private long expireTime;

    public String getKey(String... suffix) {
        StringBuilder tmpSuffix = new StringBuilder();
        if (suffix != null && suffix.length > 0) {
            for (String str : suffix) {
                if (StringUtils.isNotBlank(str)) {
                    tmpSuffix.append("_").append(str);
                }
            }
        }
        return WebConstant.REDIS_KEY_PREFIX + this.name() + tmpSuffix;
    }
}
