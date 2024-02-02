package com.webchat.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;


@Slf4j
public class FileConvertUtil {

    /***
     * MultipartFile转换为二进制
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static byte[] convertMultipartFileToByteArray(MultipartFile multipartFile) {
        byte[] binary = new byte[0];
        try {
            binary = multipartFile.getBytes();
        } catch (Exception e) {
            log.error("convert MultipartFile to byte array error.", e);
        }
        return binary;
    }
}
