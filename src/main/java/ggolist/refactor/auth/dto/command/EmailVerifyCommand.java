package ggolist.refactor.auth.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EmailVerifyCommand {
    private String email;
    private String authCode;

    public static EmailVerifyCommand of(String email, String authCode) {
        return EmailVerifyCommand.builder()
                .email(email)
                .authCode(authCode)
                .build();
    }
}