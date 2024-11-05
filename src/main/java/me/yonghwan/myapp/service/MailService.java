package me.yonghwan.myapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yonghwan.myapp.common.codes.ErrorCode;
import me.yonghwan.myapp.config.exception.BusinessException;
import me.yonghwan.myapp.config.mail.MailProperties;
import me.yonghwan.myapp.dto.EmailDto;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    private final TemplateEngine templateEngine;
    private final RedisService redisService;

    private String ePw;

    /**
     * 인증 메일 송신
     * @param email
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */

    public void sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = getMimeMessage(email);
        if (!redisService.getValues(email).isEmpty()) {
            log.info("## 인증번호 전송");
            log.info(ePw);
            javaMailSender.send(message);
            redisService.setValues(email,ePw, Duration.ofMinutes(3));
        } else {
            log.info("## 인증메일 존재 전송 불가 만료후 재전송");
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }


    private MimeMessage getMimeMessage(String email) throws MessagingException, UnsupportedEncodingException {
        ePw = createKey();
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(new InternetAddress(mailProperties.getFromMail(), mailProperties.getUsername(),"UTF-8"));
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("회원가입 인증 코드입니다.");

        Context context = new Context();
        context.setVariable("ePw", ePw);

        String html = templateEngine.process("email", context);
        helper.setText(html, true);
        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);
            switch (index) {
                case 0:
                    key.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (rnd.nextInt(26) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }

    /**
     * 인증 코드 확인
     * @param request
     * @return
     */
    public boolean confirmEmail(EmailDto request) {
        String findCode = redisService.getValues(request.getEmail());
        log.info("## 이메일과 코드가 일치하는지 확인");
        if (request.getCode().equals(findCode)) {
            log.info("## 인증 성공");
            redisService.deleteValues(request.getEmail());
            return true;
        }
        log.info("## 인증 실패");
        return false;
    }
}

