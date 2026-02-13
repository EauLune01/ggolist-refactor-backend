package ggolist.refactor.place.dto.result;

import ggolist.refactor.place.domain.Category;
import ggolist.refactor.place.dto.query.CategoryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryContentResult {

    private Category category;
    private List<CategoryItem> items;

    public static CategoryContentResult of(Category category, List<CategoryItem> items) {
        return new CategoryContentResult(category, items);
    }
}

