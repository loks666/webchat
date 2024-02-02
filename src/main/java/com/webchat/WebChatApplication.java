package com.webchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@ServletComponentScan
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.webchat.repository")
@EnableAsync
public class WebChatApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebChatApplication.class, args);

        String sb =
				"=======================================================================" +
                "\n" +
                "=========== webchat初始化成功！！请登录：http://localhost:8101 ============" +
                "\n" +
                "=======================================================================";
		System.out.println(sb);
	}
}
