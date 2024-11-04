package me.yonghwan.myapp.config.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yonghwan.myapp.service.MailService;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
@Component
@Slf4j
public class MailSendEventListener {
    public void listen(MailSendApplicationEvent event){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MailService.codeStorage.remove(event.getEmail());
                timer.cancel();
                log.info("3분 지남. 자동 삭제");
                System.out.println(MailService.codeStorage);
            }
        }, 3 * 60 * 1000);
    }
}
