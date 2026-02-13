package ggolist.refactor.place.dto.response;

import ggolist.refactor.place.dto.result.EventSummaryResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class EventSummaryResponse {
    private Long id;
    private String name;
    private String thumbnail;
    private String description;
    private int likeCount;
    private boolean liked;
    private LocalDate startDate;
    private LocalDate endDate;

    public static EventSummaryResponse from(EventSummaryResult result) {
        return EventSummaryResponse.builder()
                .id(result.getId())
                .name(result.getName())
                .thumbnail(result.getThumbnail())
                .description(result.getDescription())
                .likeCount(result.getLikeCount())
                .liked(result.isLiked())
                .startDate(result.getStartDate())
                .endDate(result.getEndDate())
                .build();
    }
}
