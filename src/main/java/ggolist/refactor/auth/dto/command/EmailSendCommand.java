package ggolist.refactor.auth.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EmailSendCommand {

    private String email;

    public static EmailSendCommand of(String email) {
        return EmailSendCommand.builder()
                .email(email)
                .build();
    }
}