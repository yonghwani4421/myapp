package me.yonghwan.myapp.config.mail;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MailSendApplicationEvent extends ApplicationEvent {

    private String email;
    private String code;
    public MailSendApplicationEvent(Object source, String email, String code){
        super(source);
        this.email = email;
        this.code = code;
    }
}
