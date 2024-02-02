package com.webchat.common.util;

import com.webchat.common.constants.WebConstant;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MD5Utils {

    /**
     * 随机产生一个MD5 16进制的字符串 跟date uuid有关。
     *
     * @param params
     *
     * @return
     */
    public static String randomMD5HexString(String... params) {
        StringBuffer bf = new StringBuffer();
        bf.append(DateUtils.getCurrentFormatDate(DateUtils.YYYYMMDDHHMMSSSS));

        if (params != null && params.length > 0) {
            for (String param : params) {
                bf.append(param);
            }
        }
        bf.append(UUID.randomUUID().toString());
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(bf.toString().getBytes("UTF-8"));
            return str2HexStr(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("生成密码："+MD5Utils.md5("admin666666".concat(WebConstant.MD5_SALT)));
    }

    public static String md5(String src) {
        if (StringUtils.isBlank(src)) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(src.getBytes("UTF-8"));
            return str2HexStr(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5(byte[] bytes) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(bytes);
            return str2HexStr(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String str2HexStr(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        char[] chars = "0123456789abcdef".toCharArray();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }
}
