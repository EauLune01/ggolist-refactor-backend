package ggolist.refactor.favorite.dto.response;

import ggolist.refactor.favorite.dto.result.FavoriteResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private boolean liked;
    private int likeCount;

    public static FavoriteResponse from(FavoriteResult result) {
        return FavoriteResponse.builder()
                .id(result.getId())
                .liked(result.isLiked())
                .likeCount(result.getLikeCount())
                .build();
    }
}
