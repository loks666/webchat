package com.webchat.common.helper;

import com.webchat.common.constants.CookieConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * @Author by loks666 on GitHub
 */
public class SessionHelper {

    /**
     * thread local实体类
     */
    private static ThreadLocal<ConcurrentHashMap<String, Object>> threadLocalHolder = new ThreadLocal<>();

    /**
     * IP
     */
    private static final String SESSION_KEY_IP = "CURRENT-IP";

    /**
     * 请求UA信息KEY
     */
    private static final String SESSION_KEY_USER_AGENT = "User-Agent";

    /**
     * TraceId
     */
    private static final String SESSION_KEY_TRACE_ID = "TRACE-ID";

    private static final String SESSION_KEY_TASK_ID = "TASK-ID";


    public static void setClientUserInfo(String userId, String ip, String taskId) {
        setUserId(userId);
        setClientIP(ip);
        setTaskId(taskId);
    }

    /**
     * 设置登录用户，谨慎使用！会覆盖原来的userId，一般在拦截器里使用。
     */
    public static void setUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            setAttribute(CookieConstants.C_U_USER_COOKIE_KEY, userId);
        }
    }

    /***
     * 设置客户端IP
     * @param ip
     */
    public static void setClientIP(String ip) {
        if (StringUtils.isNotBlank(ip)) {
            setAttribute(SESSION_KEY_IP, ip);
        }
    }

    /***
     * 设置TraceId
     * @param traceId
     */
    public static void setTraceId(String traceId) {
        if (StringUtils.isNotBlank(traceId)) {
            setAttribute(SESSION_KEY_TRACE_ID, traceId);
        }
    }

    /***
     * 设置任务ID
     * @param taskId
     */
    public static void setTaskId(String taskId) {
        if (StringUtils.isNotBlank(taskId)) {
            setAttribute(SESSION_KEY_TASK_ID, taskId);
        }
    }

    /**
     * 获取当前登录USER ID
     * @return
     */
    public static String getCurrentUserId() {
        Object userIdObj = getAttribute(CookieConstants.C_U_USER_COOKIE_KEY);
        return userIdObj == null ? null : userIdObj.toString();
    }

    /***
     * 获取客户端IP
     * @return
     */
    public static String getCurrentClientIP() {
        Object userIdObj = getAttribute(SESSION_KEY_IP);
        return userIdObj == null ? null : userIdObj.toString();
    }

    /***
     * 获取客户端UA信息
     * @return
     */
    public static String getCurrentUserAgent() {
        Object userIdObj = getAttribute(SESSION_KEY_USER_AGENT);
        return userIdObj == null ? null : userIdObj.toString();
    }

    /**
     * 清除本线程保存的数据,不要设置为null，会有内存泄漏
     */
    public static void clear() {
        threadLocalHolder.remove();
    }

    /**
     * 获取本地的session,只是一个线程的操作，不会出现多线程问题。
     * @return
     */
    private static Map<String, Object> getLocalSession() {
        if (threadLocalHolder.get() == null) {
            threadLocalHolder.set(new ConcurrentHashMap<>());
        }
        return threadLocalHolder.get();
    }

    private static void setAttribute(String key, Object value) {
        getLocalSession().put(key, value);
    }

    private static Object getAttribute(String key) {
        return getLocalSession().get(key);
    }
}
