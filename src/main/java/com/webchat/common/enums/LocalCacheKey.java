package com.webchat.common.enums;

/**
 * @Author: by loks666 on GitHub
 * @Date: 1.4.22 11:12 下午
 */
public enum LocalCacheKey {

    /***
     * 系统配置本地缓存KEY
     */
    SYSTEM_CONFIG,

    /***
     * 搜索引擎配置本地缓存
     */
    ENGINE_CONFIG,

    /***
     * 友情链接
     */
    FLINK,

    /***
     * 核心数据缓存
     */
    CORE,

    /***
     * 用户自定义导航
     */
    USER_NAV;

    public String getKey() {
        return this.name();
    }
}
