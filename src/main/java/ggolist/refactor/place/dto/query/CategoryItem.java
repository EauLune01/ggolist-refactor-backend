package ggolist.refactor.place.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CategoryItem {
    private String type;
    private Long id;
    private String name;
    private String thumbnail;
    private int likeCount;
    private boolean liked;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @QueryProjection
    public CategoryItem(String type, Long id, String name, String thumbnail,
                        int likeCount, String description, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.likeCount = likeCount;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.liked = false;
    }

    public void updateLiked(boolean liked) {
        this.liked = liked;
    }
}
