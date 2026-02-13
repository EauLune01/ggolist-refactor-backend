package ggolist.refactor.favorite.service;

import ggolist.refactor.favorite.dto.query.FavoriteItem;
import ggolist.refactor.favorite.dto.result.FavoriteItemResult;
import ggolist.refactor.favorite.dto.result.FavoriteResult;
import ggolist.refactor.favorite.repository.FavoriteEventRepository;
import ggolist.refactor.favorite.repository.FavoritePopupRepository;
import ggolist.refactor.favorite.repository.FavoriteQueryRepository;
import ggolist.refactor.favorite.repository.FavoriteStoreRepository;
import ggolist.refactor.global.exception.place.PlaceNotFoundException;
import ggolist.refactor.place.domain.Event;
import ggolist.refactor.place.domain.Popup;
import ggolist.refactor.place.domain.Store;
import ggolist.refactor.place.repository.EventRepository;
import ggolist.refactor.place.repository.PopupRepository;
import ggolist.refactor.place.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteQueryService {

    private final FavoriteQueryRepository favoriteQueryRepository;
    private final FavoriteStoreRepository favoriteStoreRepository;
    private final FavoriteEventRepository favoriteEventRepository;
    private final FavoritePopupRepository favoritePopupRepository;
    private final StoreRepository storeRepository;
    private final EventRepository eventRepository;
    private final PopupRepository popupRepository;

    public FavoriteResult getStoreFavoriteStatus(Long userId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new PlaceNotFoundException("가게를 찾을 수 없습니다."));

        boolean isLiked = favoriteStoreRepository.existsByUserIdAndStoreId(userId, storeId);

        return FavoriteResult.of(storeId, isLiked, store.getLikeCount());
    }

    public FavoriteResult getEventFavoriteStatus(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new PlaceNotFoundException("이벤트를 찾을 수 없습니다."));

        boolean isLiked = favoriteEventRepository.existsByUserIdAndEventId(userId, eventId);

        return FavoriteResult.of(eventId, isLiked, event.getLikeCount());
    }

    public FavoriteResult getPopupFavoriteStatus(Long userId, Long popupId) {
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new PlaceNotFoundException("팝업을 찾을 수 없습니다."));

        boolean isLiked = favoritePopupRepository.existsByUserIdAndPopupId(userId, popupId);

        return FavoriteResult.of(popupId, isLiked, popup.getLikeCount());
    }

    public Slice<FavoriteItemResult> getAllFavorites(Long userId, Pageable pageable) {
        Slice<FavoriteItem> slice = favoriteQueryRepository.findAllFavorites(userId, pageable);
        return slice.map(FavoriteItemResult::from);
    }

}
