package com.webchat.common.util;

import org.apache.commons.lang3.StringUtils;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FileUtil {

    private static FileNameMap fileNameMap = null;

    /***
     * 生成上传文件的文件名称
     * @param oriFileName
     * @return
     */
    public static String generateFileName(String oriFileName) {
        StringBuffer bf = new StringBuffer();
        bf.append(UUID.randomUUID().toString().replaceAll("-", ""));
        String type = null;
        if (StringUtils.isNotBlank(oriFileName)) {
            String[] fileNames = oriFileName.split("\\.");
            bf.append("_");
            bf.append(stringFilter(fileNames[0]));
            if (fileNames.length > 1) {
                type = "." + fileNames[1];
            }
        }

        String fileName = bf.toString() + "/" + type;
        fileName = fileName.replaceAll("/", "");
        fileName = fileName.replaceAll("\n", "");
        return fileName;

    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String getMimeType(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        return getFileNameMap().getContentTypeFor(fileName);
    }

    /**
     * 获得文件URL,集群环境，拼装相对主机名的URL
     *
     * @param fileURL
     * @param remoteHost
     *
     * @return
     */
    public static String getFileURL(String fileURL, String remoteHost) {
        if (StringUtils.isNotBlank(fileURL) && !fileURL.contains("http")) {
            fileURL = "http://" + remoteHost + ":" + fileURL;
        }

        return fileURL;
    }

    private static FileNameMap getFileNameMap() {
        if (fileNameMap == null) {
            fileNameMap = URLConnection.getFileNameMap();
        }
        return fileNameMap;
    }
}