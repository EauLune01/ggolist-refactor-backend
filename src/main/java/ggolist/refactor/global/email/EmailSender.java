package ggolist.refactor.global.email;

import ggolist.refactor.global.exception.mail.EmailSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender mailSender;

    public void send(SimpleMailMessage message) {
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendException("인증 이메일 발송에 실패했습니다.");
        }
    }
}