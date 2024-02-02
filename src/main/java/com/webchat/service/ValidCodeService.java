package com.webchat.service;

import com.webchat.common.constants.WebConstant;
import com.webchat.common.exception.AuthException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: by loks666 on GitHub
 * @Date: 6.11.18 2:21 上午
 */
@Service
public class ValidCodeService {

    @Autowired
    private SessionService sessionService;

    /***
     * 刷新验证码
     */
    public void refreshCode(String code) {
        // Session 保存下来
        sessionService.set(WebConstant.PIC_VALID_CODE_SESSION_KEY, code);
    }

    /**
     * 数字验证码核验
     * @param code
     */
    public void validCode(String code) {
        if(StringUtils.isBlank(code)) {
            throw new AuthException("图形验证码校验失败");
        }
        // Session 保存下来
        if (code.equals(sessionService.get(WebConstant.PIC_VALID_CODE_SESSION_KEY))) {
            return;
        }
        throw new AuthException("图形验证码校验失败");
    }
}
