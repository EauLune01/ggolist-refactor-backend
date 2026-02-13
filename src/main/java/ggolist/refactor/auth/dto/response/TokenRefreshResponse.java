package ggolist.refactor.auth.dto.response;

import ggolist.refactor.auth.dto.result.TokenRefreshResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;

    public static TokenRefreshResponse from(TokenRefreshResult result) {
        return TokenRefreshResponse.builder()
                .accessToken(result.getAccessToken())
                .refreshToken(result.getRefreshToken())
                .build();
    }
}
