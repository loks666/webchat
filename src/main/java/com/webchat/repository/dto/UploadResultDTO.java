package com.webchat.repository.dto;

import lombok.Data;

@Data
public class UploadResultDTO {

    private Boolean success;

    /***
     * 上传OSS URL
     */
    private String url;

    /**
     * 上传OSS后的随机文件名称
     */
    private String fileName;

    /***
     * 文件大小, KB
     */
    private Long fileSize;

    private String extName;

    public UploadResultDTO() {
        this.success = true;
    }

    public UploadResultDTO(boolean success) {
        this.success = success;
    }
}
