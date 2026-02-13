package ggolist.refactor.global.auth.jwt;

import ggolist.refactor.global.exception.auth.InvalidTokenException;
import ggolist.refactor.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    private final SecretKey key;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    private static final String AUTHORITIES_KEY = "auth";

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidity,
            UserDetailsService userDetailsService) {

        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.accessTokenValidity = accessTokenValidity * 1000;
        this.refreshTokenValidity = refreshTokenValidity * 1000;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Access Token 생성
     */
    public String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return buildToken(user.getId().toString(), user.getEmail(), authorities, accessTokenValidity);
    }

    /**
     * Refresh Token 생성 (보안상 최소 정보만 포함)
     */
    public String createRefreshToken(User user) {
        return buildToken(user.getId().toString(), null, null, refreshTokenValidity);
    }

    /**
     * 토큰에서 Authentication 객체 추출 (SecurityContext 보관용)
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (claims.get("email") == null) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }

        String email = claims.get("email").toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 토큰의 남은 만료 시간 조회
     */
    public long getRemainingTime(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    /**
     * Claims 파싱
     */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰에서 userId 추출
     */
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);

        String subject = claims.getSubject();

        if (subject == null) {
            throw new InvalidTokenException("토큰에 사용자 정보가 없습니다.");
        }
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            throw new InvalidTokenException("토큰의 사용자 ID 형식이 올바르지 않습니다.");
        }
    }

    /**
     * JWT 만드는 Helper Method
     */
    private String buildToken(String sub, String email, String auth, long validity) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .subject(sub)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validity))
                .signWith(key);
        if (email != null) builder.claim("email", email);
        if (auth != null) builder.claim(AUTHORITIES_KEY, auth);
        return builder.compact();
    }
}