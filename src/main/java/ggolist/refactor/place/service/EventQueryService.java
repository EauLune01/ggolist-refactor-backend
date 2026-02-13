package ggolist.refactor.place.service;

import ggolist.refactor.place.domain.Filter;
import ggolist.refactor.place.dto.result.EventSummaryResult;
import ggolist.refactor.place.repository.EventRepository;
import ggolist.refactor.favorite.repository.FavoriteEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventQueryService {
    private final EventRepository eventRepository;
    private final FavoriteEventRepository favoriteEventRepository;

    public Slice<EventSummaryResult> getEventsByFilter(Filter filter, Long userId, Pageable pageable) {
        Filter targetFilter = (filter == null) ? Filter.ONGOING : filter;
        Slice<EventSummaryResult> slice = eventRepository.findEventsByFilter(targetFilter, LocalDate.now(), pageable);
        if (slice.hasContent()) {
            updateLikedStatus(slice.getContent(), userId);
        }
        return slice;
    }

    /******************** Helper Method ********************/
    private void updateLikedStatus(List<EventSummaryResult> results, Long userId) {
        if (userId == null || results.isEmpty()) {
            return;
        }

        Set<Long> myEventIds = favoriteEventRepository.findEventIdsByUserId(userId);
        results.forEach(r -> r.updateLiked(myEventIds.contains(r.getId())));
    }
}
