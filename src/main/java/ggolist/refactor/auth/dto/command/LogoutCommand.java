package ggolist.refactor.auth.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LogoutCommand {
    private String accessToken;
    private Long userId;

    public static LogoutCommand of(String accessToken, Long userId) {
        return LogoutCommand.builder()
                .accessToken(accessToken)
                .userId(userId)
                .build();
    }
}
