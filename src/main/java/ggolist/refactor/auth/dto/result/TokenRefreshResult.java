package ggolist.refactor.auth.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenRefreshResult {
    private String accessToken;
    private String refreshToken;

    public static TokenRefreshResult of(String accessToken, String refreshToken) {
        return TokenRefreshResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}