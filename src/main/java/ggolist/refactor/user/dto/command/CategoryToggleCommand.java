package ggolist.refactor.user.dto.command;

import ggolist.refactor.place.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryToggleCommand {
    private Long userId;
    private Category category;

    public static CategoryToggleCommand of(Long userId, Category category) {
        return new CategoryToggleCommand(userId, category);
    }
}
