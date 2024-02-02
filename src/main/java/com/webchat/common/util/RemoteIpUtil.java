package com.webchat.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class RemoteIpUtil {

    public static final String COMMA = ",";

    private static final String REMOTE_IP_HEADER_FLAG = "x-forwarded-for";

    /**
     * get Real RemoteIp
     *
     * @param request
     * @return
     */
    public static String getRemoteIpByRequest(HttpServletRequest request) {
        String requestIp = request.getHeader(REMOTE_IP_HEADER_FLAG);

        if (StringUtils.isBlank(requestIp)) {
            return request.getRemoteAddr();
        }

        int lastIndex = requestIp.lastIndexOf(COMMA);
        if (lastIndex < 0) {
            return requestIp;
        }
        return requestIp.substring(lastIndex + 1);
    }

    /**
     * 获取本地的HostName
     *
     * @return
     */
    public static String getLocalHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}