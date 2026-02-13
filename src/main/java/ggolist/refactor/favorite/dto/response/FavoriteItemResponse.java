package ggolist.refactor.favorite.dto.response;

import ggolist.refactor.favorite.dto.result.FavoriteItemResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FavoriteItemResponse {
    private Long id;
    private String type;
    private boolean liked;
    private int likeCount;
    private String name;

    public static FavoriteItemResponse from(FavoriteItemResult result) {
        return FavoriteItemResponse.builder()
                .id(result.getId())
                .type(result.getType())
                .liked(result.isLiked())
                .likeCount(result.getLikeCount())
                .name(result.getName())
                .build();
    }
}
