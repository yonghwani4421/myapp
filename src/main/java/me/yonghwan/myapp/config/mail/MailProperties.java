package me.yonghwan.myapp.config.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("mail")
public class MailProperties {

    // SMTP 서버
    private String host;

    // 포트번호
    private int port;

    // 계정
    private String username;

    // 비밀번호
    private String password;

    // 메일연결자
    private String supplier;

    // 발신자 메일
    private String fromMail;

    // tls 설정
//    private String socketFactoryClass;
}