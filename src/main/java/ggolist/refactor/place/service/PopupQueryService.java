package ggolist.refactor.place.service;

import ggolist.refactor.place.dto.result.PopupSummaryResult;
import ggolist.refactor.place.repository.PopupRepository;
import ggolist.refactor.favorite.repository.FavoritePopupRepository;
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
public class PopupQueryService {
    private final PopupRepository popupRepository;
    private final FavoritePopupRepository favoritePopupRepository;

    public Slice<PopupSummaryResult> getWeeklyPopups(Long userId, Pageable pageable) {
        Slice<PopupSummaryResult> slice = popupRepository.findWeeklyPopups(LocalDate.now(), pageable);

        if (slice.hasContent() && userId != null) {
            updateLikedStatus(slice.getContent(), userId);
        }
        return slice;
    }

    private void updateLikedStatus(List<PopupSummaryResult> results, Long userId) {
        Set<Long> myLikedIds = favoritePopupRepository.findPopupIdsByUserId(userId);
        results.forEach(r -> r.updateLiked(myLikedIds.contains(r.getId())));
    }
}
