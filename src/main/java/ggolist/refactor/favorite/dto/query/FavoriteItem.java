package ggolist.refactor.favorite.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FavoriteItem {
    private String type;
    private Long id;
    private String name;
    private int likeCount;
    private boolean liked;
    private LocalDateTime createdAt;

    @QueryProjection
    public FavoriteItem(String type, Long id, String name, int likeCount, LocalDateTime createdAt) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.likeCount = likeCount;
        this.liked = true;
        this.createdAt = createdAt;
    }
}
