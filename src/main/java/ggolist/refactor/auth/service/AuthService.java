package ggolist.refactor.auth.service;

import ggolist.refactor.auth.dto.command.LoginCommand;
import ggolist.refactor.auth.dto.command.SignupCommand;
import ggolist.refactor.auth.dto.command.TokenRefreshCommand;
import ggolist.refactor.auth.dto.result.LoginResult;
import ggolist.refactor.auth.dto.result.TokenRefreshResult;
import ggolist.refactor.global.auth.jwt.JwtTokenProvider;
import ggolist.refactor.global.exception.auth.InvalidTokenException;
import ggolist.refactor.global.exception.auth.TokenMismatchException;
import ggolist.refactor.global.exception.mail.EmailDuplicateException;
import ggolist.refactor.global.exception.mail.EmailNotVerifiedException;
import ggolist.refactor.global.exception.user.InvalidPasswordException;
import ggolist.refactor.global.exception.user.UserNotFoundException;
import ggolist.refactor.user.domain.User;
import ggolist.refactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate redisTemplate;


    public void signup(SignupCommand command) {
        validateSignupEmail(command.getEmail());
        String encodedPassword = passwordEncoder.encode(command.getPassword());
        saveUser(command, encodedPassword);
        redisTemplate.delete(RedisMailConstants.VERIFIED_PREFIX + command.getEmail());
    }


    public LoginResult login(LoginCommand command) {
        User user = getUserByEmail(command.getEmail());
        checkPassword(command.getPassword(), user.getPassword());
        return issueTokens(user);
    }

    public TokenRefreshResult refreshToken(TokenRefreshCommand command) {
        User user = getValidatedUserFromRefreshToken(command.getRefreshToken());
        return reissueTokens(user);
    }

    public void logout(Long userId, String accessToken) {
        registerBlacklist(accessToken);
        getUserById(userId).invalidateRefreshToken();
    }

    /******************** Helper Method ********************/

    private void validateSignupEmail(String email) {
        validateDuplicateEmail(email);
        validateEmailVerification(email);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailDuplicateException("이미 가입된 이메일이 있습니다.");
        }
    }

    private void validateEmailVerification(String email) {
        String isVerified = redisTemplate.opsForValue().get(RedisMailConstants.VERIFIED_PREFIX + email);

        if (isVerified == null || !isVerified.equals("TRUE")) {
            throw new EmailNotVerifiedException("이메일 인증이 완료되지 않았거나 인증 시간이 만료되었습니다.");
        }
    }

    private void saveUser(SignupCommand command, String encodedPassword) {
        User user = User.create(
                command.getEmail(),
                encodedPassword,
                command.getRole(),
                command.getCategories()
        );
        userRepository.save(user);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 계정이 없습니다."));
    }

    private void checkPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    private LoginResult issueTokens(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        user.updateRefreshToken(refreshToken);

        return LoginResult.of(accessToken, refreshToken, user.getRole().name());
    }

    private User getValidatedUserFromRefreshToken(String refreshToken) {
        Long userId = jwtTokenProvider.getUserId(refreshToken);
        User user = getUserById(userId);

        verifyRefreshToken(refreshToken, user.getRefreshToken());

        return user;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private void verifyRefreshToken(String requestToken, String storedToken) {
        validateRefreshToken(requestToken);
        checkTokenMatch(requestToken, storedToken);
    }
    private void validateRefreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 리프레시 토큰입니다.");
        }
    }

    private void checkTokenMatch(String requestToken, String storedToken) {
        if (!requestToken.equals(storedToken)) {
            throw new TokenMismatchException("리프레시 토큰 정보가 일치하지 않습니다. 다시 로그인해주세요.");
        }
    }
    private TokenRefreshResult reissueTokens(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

        user.updateRefreshToken(newRefreshToken);

        return TokenRefreshResult.of(newAccessToken, newRefreshToken);
    }

    private void registerBlacklist(String accessToken) {
        long remainingTime = jwtTokenProvider.getRemainingTime(accessToken);

        if (remainingTime > 0) {
            redisTemplate.opsForValue().set(
                    "blacklist:" + accessToken,
                    "logout",
                    remainingTime,
                    TimeUnit.MILLISECONDS
            );
        }
    }
}
