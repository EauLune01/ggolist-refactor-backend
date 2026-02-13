package ggolist.refactor.user.dto.result;


import ggolist.refactor.place.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryListResult {
    private List<Category> categories;

    public static CategoryListResult of(List<Category> categories) {
        return new CategoryListResult(categories);
    }
}
