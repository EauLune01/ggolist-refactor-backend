package ggolist.refactor.auth.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenRefreshCommand {

    private String refreshToken;

    public static TokenRefreshCommand of(String refreshToken) {
        return TokenRefreshCommand.builder()
                .refreshToken(refreshToken)
                .build();
    }
}
