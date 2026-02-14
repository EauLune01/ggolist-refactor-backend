package ggolist.refactor.auth.service;

import ggolist.refactor.auth.dto.command.EmailSendCommand;
import ggolist.refactor.global.email.EmailSender;
import ggolist.refactor.global.exception.mail.EmailDuplicateException;
import ggolist.refactor.global.exception.mail.VerificationCodeMismatchException;
import ggolist.refactor.global.exception.mail.VerificationCodeNotFoundException;
import ggolist.refactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailSender emailSender;
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;

    public void sendVerificationMail(EmailSendCommand command, String authCode) {
        String email = command.getEmail();
        validateEmailRequest(email);

        SimpleMailMessage message = createMessage(email, authCode);
        emailSender.send(message);

        saveAuthCode(email, authCode);
        increaseRequestCount(email);
    }

    public void verifyCode(String email, String inputCode) {
        String savedCode = getSavedCodeOrThrow(email);
        checkCodeMismatch(inputCode, savedCode);
        completeVerification(email);
    }

    /********************Helper Methods ********************/

    private void validateEmailRequest(String email) {
        checkDuplicatedEmail(email);
        validateRequestCount(email);
    }

    private void checkDuplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailDuplicateException("이미 가입된 이메일입니다.");
        }
    }

    private void validateRequestCount(String email) {
        String key = RedisMailConstants.REQUEST_COUNT_PREFIX + email;
        String countVal = redisTemplate.opsForValue().get(key);

        if (countVal != null && Integer.parseInt(countVal) >= RedisMailConstants.MAX_REQUEST_COUNT) {
            throw new RuntimeException("일일 메일 요청 횟수(5회)를 초과했습니다. 24시간 후 다시 시도해주세요.");
        }
    }

    private SimpleMailMessage createMessage(String email, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[ggolist] 회원가입 인증번호 안내");
        message.setText("인증번호는 [" + authCode + "] 입니다. (5분 뒤에 만료됩니다.)");
        return message;
    }

    private void saveAuthCode(String email, String authCode) {
        redisTemplate.opsForValue().set(
                RedisMailConstants.AUTH_PREFIX + email,
                authCode,
                RedisMailConstants.AUTH_TTL_MINUTE,
                TimeUnit.MINUTES
        );
    }

    private void increaseRequestCount(String email) {
        String key = RedisMailConstants.REQUEST_COUNT_PREFIX + email;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            redisTemplate.expire(key, RedisMailConstants.BLOCK_24_HOURS, TimeUnit.HOURS);
        }
    }

    private String getSavedCodeOrThrow(String email) {
        String savedCode = redisTemplate.opsForValue().get(RedisMailConstants.AUTH_PREFIX + email);
        if (savedCode == null) {
            throw new VerificationCodeNotFoundException("인증 시간이 만료되었거나 요청 이력이 없습니다.");
        }
        return savedCode;
    }

    private void checkCodeMismatch(String inputCode, String savedCode) {
        if (!savedCode.equals(inputCode)) {
            throw new VerificationCodeMismatchException("인증번호가 일치하지 않습니다.");
        }
    }

    private void completeVerification(String email) {
        redisTemplate.delete(RedisMailConstants.AUTH_PREFIX + email);

        redisTemplate.opsForValue().set(
                RedisMailConstants.VERIFIED_PREFIX + email,
                "TRUE",
                10,
                TimeUnit.MINUTES
        );
    }
}
