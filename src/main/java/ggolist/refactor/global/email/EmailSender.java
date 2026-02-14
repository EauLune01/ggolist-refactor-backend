package ggolist.refactor.global.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

    private final JavaMailSender mailSender;

    @Async("emailExecutor")
    public void send(SimpleMailMessage message) {
        try {
            mailSender.send(message);
            log.info("인증 메일 발송 성공: {}", message.getTo()[0]);
        } catch (Exception e) {
            log.error("메일 발송 중 오류 발생 (To: {}): {}", message.getTo()[0], e.getMessage());
        }
    }
}