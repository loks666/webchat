package com.webchat.config.configuration;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class MultiPartFileConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize(DataSize.ofBytes(100 * 2014 * 1024));
        // 总上传数据大小
        factory.setMaxRequestSize(DataSize.ofBytes(100 * 2014 * 1024));
        return factory.createMultipartConfig();
    }
}
