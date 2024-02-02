package com.webchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: by loks666 on GitHub
 * @Date: 6.11.18 2:21 上午
 */
@Service
public class SessionService {

    @Autowired
    private HttpServletRequest request;

    /***
     * 创建SESSIOn
     * @param key
     * @param val
     * @return
     */
    public boolean set(String key, String val) {
        request.getSession().setAttribute(key, val);
        return true;
    }

    /***
     * 查询SESSION
     * @param key
     * @return
     */
    public String get(String key) {
        Object valObj = request.getSession().getAttribute(key);
        return valObj == null ? null : valObj.toString();
    }
}
