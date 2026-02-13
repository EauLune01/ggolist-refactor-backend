package ggolist.refactor.place.dto.result;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PopupSummaryResult {
    private Long id;
    private String name;
    private String thumbnail;
    private String description;
    private int likeCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean liked;

    @QueryProjection
    public PopupSummaryResult(Long id, String name, String thumbnail, String description,
                              int likeCount, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.description = description;
        this.likeCount = likeCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateLiked(boolean liked) {
        this.liked = liked;
    }
}
