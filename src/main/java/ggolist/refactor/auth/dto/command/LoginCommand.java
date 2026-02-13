package ggolist.refactor.auth.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginCommand {
    private String email;
    private String password;

    public static LoginCommand of(String email, String password) {
        return LoginCommand.builder()
                .email(email)
                .password(password)
                .build();
    }
}
