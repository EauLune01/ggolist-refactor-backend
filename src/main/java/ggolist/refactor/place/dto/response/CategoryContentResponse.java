package ggolist.refactor.place.dto.response;

import ggolist.refactor.place.domain.Category;
import ggolist.refactor.place.dto.query.CategoryItem;
import ggolist.refactor.place.dto.result.CategoryContentResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CategoryContentResponse{
    private Category category;
    private List<CategoryItem> items;

    public static CategoryContentResponse from(CategoryContentResult result) {
        return CategoryContentResponse.builder()
                .category(result.getCategory())
                .items(result.getItems())
                .build();
    }
}
