package ggolist.refactor.auth.dto.request;

import ggolist.refactor.auth.dto.command.EmailVerifyCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "인증번호는 필수입니다.")
    private String authCode;

    public EmailVerifyCommand toCommand() {
        return EmailVerifyCommand.of(this.email, this.authCode);
    }
}
