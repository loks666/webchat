package com.webchat.common.util.obj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

/**
 * 验证码生成结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PicValidCodeResult {

    private String code;
    private BufferedImage image;
}