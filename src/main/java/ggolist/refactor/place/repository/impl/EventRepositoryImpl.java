package ggolist.refactor.place.repository.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggolist.refactor.place.domain.Filter;
import ggolist.refactor.place.dto.result.EventSummaryResult;
import ggolist.refactor.place.dto.result.QEventSummaryResult;
import ggolist.refactor.place.repository.custom.EventRepositoryCustom;
import ggolist.refactor.global.utils.SliceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

import static ggolist.refactor.place.domain.QEvent.event;


@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<EventSummaryResult> findEventsByFilter(Filter filter, LocalDate today, Pageable pageable) {
        List<EventSummaryResult> content = queryFactory
                .select(new QEventSummaryResult(
                        event.id,
                        event.name,
                        event.thumbnail,
                        event.description,
                        event.likeCount,
                        event.startDate,
                        event.endDate
                ))
                .from(event)
                .where(buildFilterCondition(filter, today))
                .orderBy(buildOrderSpecifier(filter), event.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return SliceUtils.checkLastPage(pageable, content);
    }

    /******************** Helper Method ********************/

    private BooleanExpression buildFilterCondition(Filter filter, LocalDate today) {
        return switch (filter) {
            case POPULAR, ONGOING -> event.startDate.loe(today).and(event.endDate.goe(today));
            case CLOSING_TODAY -> event.endDate.eq(today);
            case UPCOMING -> event.startDate.gt(today);
        };
    }

    private OrderSpecifier<?> buildOrderSpecifier(Filter filter) {
        return switch (filter) {
            case POPULAR -> event.likeCount.desc();
            case ONGOING -> event.startDate.desc();
            case UPCOMING -> event.startDate.asc();
            case CLOSING_TODAY -> event.endDate.asc();
        };
    }
}
