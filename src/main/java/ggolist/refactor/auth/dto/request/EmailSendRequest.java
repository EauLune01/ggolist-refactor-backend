package ggolist.refactor.auth.dto.request;

import ggolist.refactor.auth.dto.command.EmailSendCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    public EmailSendCommand toCommand() {
        return EmailSendCommand.builder()
                .email(this.email)
                .build();
    }
}