package ggolist.refactor.place.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class AllCategoryListResult {
    private List<CategoryContentResult> categories;
}
