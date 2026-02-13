package ggolist.refactor.place.repository.custom;

import ggolist.refactor.place.domain.Filter;
import ggolist.refactor.place.dto.result.EventSummaryResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;

public interface EventRepositoryCustom {
    Slice<EventSummaryResult> findEventsByFilter(Filter filter, LocalDate today, Pageable pageable);
}
