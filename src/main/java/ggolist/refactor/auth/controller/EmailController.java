package ggolist.refactor.auth.controller;

import ggolist.refactor.auth.dto.command.EmailVerifyCommand;
import ggolist.refactor.auth.dto.request.EmailSendRequest;
import ggolist.refactor.auth.dto.request.EmailVerifyRequest;
import ggolist.refactor.auth.service.EmailService;
import ggolist.refactor.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@Tag(name = "이메일 인증 (Email Auth)", description = "이메일 발송 및 인증 코드 검증 API")
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "인증 메일 발송", description = "입력한 이메일로 6자리 인증번호를 발송합니다. (1일 최대 5회 제한)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 메일 발송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 가입된 이메일 또는 발송 횟수 초과"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "메일 서버 오류")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendEmail(@RequestBody @Valid EmailSendRequest request) {
        String authCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        emailService.sendVerificationMail(request.toCommand(), authCode);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "인증 메일이 발송되었습니다."));
    }

    @Operation(summary = "인증 코드 검증", description = "메일로 받은 인증번호가 유효한지 확인합니다. 성공 시 10분간 회원가입 권한이 부여됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "인증번호 불일치"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "인증 시간이 만료되었거나 요청 이력이 없음")
    })
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestBody @Valid EmailVerifyRequest request) {
        EmailVerifyCommand command = request.toCommand();
        emailService.verifyCode(command.getEmail(), command.getAuthCode());
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "이메일 인증에 성공했습니다."));
    }
}
