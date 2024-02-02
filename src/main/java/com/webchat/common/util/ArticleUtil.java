package com.webchat.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: by loks666 on GitHub
 * @Date: 27.3.22 5:21 下午
 */
public class ArticleUtil {

    /***
     * 提取html中所有的图片
     * @param html
     * @return
     */
    public static List<String> getImagesFromHtml(String html) {
        List<String> images = new ArrayList<>();
        if (StringUtils.isBlank(html)) {
            return images;
        }
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(html);
        String quote;
        String src;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
            images.add(src.replaceAll("&quot;", ""));
        }
        return images;
    }
}