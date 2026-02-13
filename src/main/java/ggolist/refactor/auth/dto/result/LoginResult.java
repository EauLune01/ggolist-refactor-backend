package ggolist.refactor.auth.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResult {
    private String accessToken;
    private String refreshToken;
    private String role;

    public static LoginResult of(String accessToken, String refreshToken, String role) {
        return LoginResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }
}
