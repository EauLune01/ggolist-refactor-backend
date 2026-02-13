package ggolist.refactor.favorite.dto.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FavoriteResult {
    private Long id;
    private boolean isLiked;
    private int likeCount;

    public static FavoriteResult of(Long id, boolean isLiked, int likeCount) {
        return FavoriteResult.builder()
                .id(id)
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }
}
