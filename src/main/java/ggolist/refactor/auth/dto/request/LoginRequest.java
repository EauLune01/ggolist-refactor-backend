package ggolist.refactor.auth.dto.request;

import ggolist.refactor.auth.dto.command.LoginCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    public LoginCommand toCommand() {
        return LoginCommand.of(this.email, this.password);
    }
}
