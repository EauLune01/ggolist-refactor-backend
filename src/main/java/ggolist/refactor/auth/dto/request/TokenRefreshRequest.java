package ggolist.refactor.auth.dto.request;

import ggolist.refactor.auth.dto.command.TokenRefreshCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {

    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    private String refreshToken;

    public TokenRefreshCommand toCommand() {
        return TokenRefreshCommand.of(this.refreshToken);
    }
}

