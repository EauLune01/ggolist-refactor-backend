package ggolist.refactor.favorite.dto.result;

import ggolist.refactor.favorite.dto.query.FavoriteItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FavoriteItemResult {
    private Long id;
    private String type;
    private String name;
    private int likeCount;
    private boolean liked;

    public static FavoriteItemResult from(FavoriteItem item) {
        return FavoriteItemResult.builder()
                .id(item.getId())
                .type(item.getType())
                .name(item.getName())
                .likeCount(item.getLikeCount())
                .liked(true)
                .build();
    }
}
