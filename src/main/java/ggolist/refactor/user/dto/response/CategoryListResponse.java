package ggolist.refactor.user.dto.response;

import ggolist.refactor.user.dto.result.CategoryListResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CategoryListResponse {
    private List<String> categories;

    public static CategoryListResponse from(CategoryListResult result) {
        return CategoryListResponse.builder()
                .categories(result.getCategories().stream()
                        .map(Enum::name)
                        .toList())
                .build();
    }
}