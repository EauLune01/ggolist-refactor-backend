package ggolist.refactor.place.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ggolist.refactor.place.dto.result.PopupSummaryResult;
import ggolist.refactor.place.dto.result.QPopupSummaryResult;
import ggolist.refactor.place.repository.custom.PopupRepositoryCustom;
import ggolist.refactor.global.utils.SliceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

import static ggolist.refactor.place.domain.QPopup.popup;


@RequiredArgsConstructor
public class PopupRepositoryImpl implements PopupRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<PopupSummaryResult> findWeeklyPopups(LocalDate today, Pageable pageable){
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);

        List<PopupSummaryResult> content = queryFactory
                .select(new QPopupSummaryResult(
                        popup.id,
                        popup.name,
                        popup.thumbnail,
                        popup.description,
                        popup.likeCount,
                        popup.startDate,
                        popup.endDate
                ))
                .from(popup)
                .where(
                        popup.startDate.loe(endOfWeek).and(popup.endDate.goe(startOfWeek))
                )
                .orderBy(popup.likeCount.desc(), popup.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return SliceUtils.checkLastPage(pageable, content);
    }
}

