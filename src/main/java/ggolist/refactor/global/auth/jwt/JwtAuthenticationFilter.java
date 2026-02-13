package ggolist.refactor.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader(JwtConstants.HEADER_STRING);
        String token = JwtUtils.stripBearerPrefix(bearerToken);
        log.info("Prefix 제거 후 토큰: {}", token);

        try {
            if (StringUtils.hasText(token)) {
                if (jwtTokenProvider.validateToken(token)) {
                    log.info("토큰 검증 성공!");

                    if (isBlacklisted(token)) {
                        log.warn("블랙리스트에 등록된 토큰입니다.");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Context 저장 성공: {}", auth.getName());
                } else {
                    log.warn("토큰 검증 실패 (validateToken == false)");
                }
            } else {
                log.info("토큰이 존재하지 않음 (비로그인 상태)");
            }
        } catch (Exception e) {
            log.error("JWT 인증 과정에서 예외 발생: ", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token));
    }
}
