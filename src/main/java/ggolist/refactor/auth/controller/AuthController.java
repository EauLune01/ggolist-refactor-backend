package ggolist.refactor.auth.controller;

import ggolist.refactor.auth.dto.command.TokenRefreshCommand;
import ggolist.refactor.auth.dto.request.LoginRequest;
import ggolist.refactor.auth.dto.request.SignupRequest;
import ggolist.refactor.auth.dto.request.TokenRefreshRequest;
import ggolist.refactor.auth.dto.response.LoginResponse;
import ggolist.refactor.auth.dto.response.TokenRefreshResponse;
import ggolist.refactor.auth.dto.result.LoginResult;
import ggolist.refactor.auth.dto.result.TokenRefreshResult;
import ggolist.refactor.auth.service.AuthService;
import ggolist.refactor.global.auth.jwt.JwtConstants;
import ggolist.refactor.global.auth.jwt.JwtUtils;
import ggolist.refactor.global.dto.response.ApiResponse;
import ggolist.refactor.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 (Auth)", description = "회원가입, 로그인, 토큰 재발급, 로그아웃 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 계정을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이메일 중복 또는 잘못된 요청")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request.toCommand());
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "회원가입이 완료되었습니다."));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 토큰을 발급받습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "비밀번호 불일치 혹은 미가입 이메일")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = authService.login(request.toCommand());
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "로그인 성공", LoginResponse.from(result)));
    }

    @Operation(summary = "토큰 재발급 (Refresh)", description = "RefreshToken을 사용하여 토큰 세트를 갱신합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 RefreshToken"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "일치하는 리프레시 토큰 정보를 찾을 수 없음")
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refresh(
            @Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResult result = authService.refreshToken(TokenRefreshCommand.of(request.getRefreshToken()));
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "토큰 재발급 성공", TokenRefreshResponse.from(result)));
    }

    @Operation(summary = "로그아웃", description = "사용 중인 토큰을 무효화합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal User user,
            @RequestHeader(value = JwtConstants.HEADER_STRING) String authHeader) {
        String accessToken = JwtUtils.stripBearerPrefix(authHeader);
        authService.logout(user.getId(), accessToken);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "로그아웃 성공"));
    }
}
