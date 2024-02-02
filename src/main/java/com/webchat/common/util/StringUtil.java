package com.webchat.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: by loks666 on GitHub
 * @Date: 22.3.22 11:23 下午
 */
public class StringUtil {


    public static String handleSpecialHtmlTag(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        content = content.replaceAll("&lt;br&gt;", "<br>");
        content = content.replaceAll("&lt;b&gt;", "<b>");
        return content;
    }

}
