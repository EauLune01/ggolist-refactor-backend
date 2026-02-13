package ggolist.refactor.auth.dto.response;

import ggolist.refactor.auth.dto.result.LoginResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String role;

    public static LoginResponse from(LoginResult result) {
        return LoginResponse.builder()
                .accessToken(result.getAccessToken())
                .refreshToken(result.getRefreshToken())
                .role(result.getRole())
                .build();
    }
}