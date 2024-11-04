package me.yonghwan.myapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yonghwan.myapp.common.codes.ErrorCode;
import me.yonghwan.myapp.config.exception.BusinessException;
import me.yonghwan.myapp.config.mail.MailProperties;
import me.yonghwan.myapp.config.mail.MailSendApplicationEvent;
import me.yonghwan.myapp.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;
    private final MemberService memberService;
    private final TemplateEngine templateEngine;
    private final RedisTemplate<String, Object> redisTemplate;

    private String ePw;
    private final ApplicationEventPublisher publisher;

    public static HashMap<String, String> codeStorage = new HashMap<>();


    public void sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
//        memberService.verifyExistEmail(email);
        ePw = createKey();
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(new InternetAddress(mailProperties.getFromMail(), mailProperties.getUsername(),"UTF-8"));
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("InddyBuddy의 회원가입 인증 코드입니다.");

        Context context = new Context();
        context.setVariable("ePw", ePw);

        String html = templateEngine.process("email", context);
        helper.setText(html, true);

//        helper.addInline("image", new ClassPathResource("static/logo.png"));

        try {
            if (!codeStorage.containsKey(email)) {
                ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                log.info("인증번호 전송");
                log.info(ePw);
                javaMailSender.send(message);
                valueOperations.set(email,ePw);
//                codeStorage.put(email, ePw);
                System.out.println(codeStorage);
                publisher.publishEvent(new MailSendApplicationEvent(this, email, ePw));
            } else {
                log.info("3분이 지나지 않았으므로 전송 불가");
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
            }
        } catch (MailException es) {
            es.printStackTrace();
            log.info("인증번호 전송 실패");
            codeStorage.remove(email);
            System.out.println(codeStorage);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }
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

    public boolean confirmEmail(EmailDto request) {
        String email = request.getEmail();
        String code = request.getCode();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String findCode = (String) valueOperations.get(email);
//        String findCode = codeStorage.get(email);
        log.info("이메일과 코드가 일치하는지 확인");
        if (code.equals(findCode)) {
            log.info("일치!!!");
            valueOperations.getAndDelete(email);
//            codeStorage.remove(email);
            System.out.println(valueOperations.toString());
            return true;
        }
        log.info("불일치!!!");
        System.out.println(valueOperations.toString());
        return false;
    }
}

