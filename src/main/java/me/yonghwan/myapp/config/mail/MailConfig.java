package me.yonghwan.myapp.config.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MailConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setJavaMailProperties(getMailProperties());
        mailSender.setDefaultEncoding("UTF-8");

        log.info("## JavaMailSender : {}", mailSender.getClass());

        return mailSender;
    }
    private Properties getMailProperties() {
        Properties pt = new Properties();
        pt.put("mail.transport.protocol", "smtp");
        pt.put("mail.smtp.auth", true);
        pt.put("mail.smtp.starttls.enable", true);
        return pt;
    }
}
