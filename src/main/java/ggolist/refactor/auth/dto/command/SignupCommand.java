package ggolist.refactor.auth.dto.command;

import ggolist.refactor.place.domain.Category;
import ggolist.refactor.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SignupCommand {

    private String email;
    private String password;
    private Role role;
    private List<Category> categories;

    public static SignupCommand of(String email, String password, Role role, List<Category> categories) {
        return new SignupCommand(email, password, role, categories);
    }
}