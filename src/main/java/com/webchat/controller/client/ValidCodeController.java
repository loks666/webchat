package com.webchat.controller.client;

import com.webchat.common.util.PicValidCodeUtil;
import com.webchat.common.util.obj.PicValidCodeResult;
import com.webchat.service.ValidCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * 图形验证码
 */
@Slf4j
@RestController
@RequestMapping("/api/valid-code")
public class ValidCodeController {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private ValidCodeService validCodeService;

    /***
     * 获取图形验证码
     */
    @GetMapping("/code")
    public void getValidCode() {
        // 生成图形验证码
        PicValidCodeResult picValidCodeResult = PicValidCodeUtil.generateCodeAndPic(true);
        // 刷新验证码
        validCodeService.refreshCode(picValidCodeResult.getCode());
        // 返回客户端
        printValidCodeImage(picValidCodeResult.getImage());
    }

    private void printValidCodeImage(BufferedImage img) {
        response.setContentType("image/png");
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        try {
            ImageIO.write(img, "png", response.getOutputStream());
        } catch (Exception e) {
            log.error("POSTER IMAGE CREATE ERROR.", e);
        }
    }
}
