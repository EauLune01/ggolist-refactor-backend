package ggolist.refactor.auth.dto.request;

import ggolist.refactor.auth.dto.command.SignupCommand;
import ggolist.refactor.place.domain.Category;
import ggolist.refactor.user.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotNull(message = "권한(Role) 설정은 필수입니다.")
    private Role role;

    private List<String> categories;

    public SignupCommand toCommand() {
        List<Category> categoryList = (categories != null) ?
                categories.stream()
                        .map(name -> Category.valueOf(name.toUpperCase()))
                        .toList() : new ArrayList<>();

        return SignupCommand.of(this.email, this.password, this.role, categoryList);
    }
}