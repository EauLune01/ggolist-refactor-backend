package ggolist.refactor.place.dto.response;

import ggolist.refactor.place.dto.result.AllCategoryListResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AllCategoryListResponse {
    private List<CategoryContentResponse> categories;

    public static AllCategoryListResponse from(AllCategoryListResult result) {
        return AllCategoryListResponse.builder()
                .categories(result.getCategories().stream()
                        .map(CategoryContentResponse::from)
                        .toList())
                .build();
    }
}
