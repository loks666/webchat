package com.webchat.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;

/***
 * ID 生成器
 */
public class IDGenerateUtil {

    public static String createId(String prefix) {
        if (StringUtils.isNotBlank(prefix)) {
            return prefix.concat("_").concat(uuid());
        }
        return uuid();
    }

    public static long genItemId() {
        // 取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        // 加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        // 如果不足两位前面补0
        String str = millis + String.format("%02d", end2);
        long id = new Long(str);
        return id;
    }

    /**
     * 生成验证码, 生成4为长度验证码
     * @return
     */
    public static String createCode() {
        return String.format("%04d",new Random().nextInt(9999));
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
