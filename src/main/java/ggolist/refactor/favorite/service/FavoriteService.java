package ggolist.refactor.favorite.service;

import ggolist.refactor.favorite.domain.FavoriteEvent;
import ggolist.refactor.favorite.domain.FavoritePopup;
import ggolist.refactor.favorite.domain.FavoriteStore;
import ggolist.refactor.favorite.repository.FavoriteEventRepository;
import ggolist.refactor.favorite.repository.FavoritePopupRepository;
import ggolist.refactor.favorite.repository.FavoriteStoreRepository;
import ggolist.refactor.global.exception.favorite.FavoriteDuplicateException;
import ggolist.refactor.global.exception.favorite.FavoriteNotFoundException;
import ggolist.refactor.global.exception.place.PlaceNotFoundException;
import ggolist.refactor.global.exception.user.UserNotFoundException;
import ggolist.refactor.place.domain.Event;
import ggolist.refactor.place.domain.Popup;
import ggolist.refactor.place.domain.Store;
import ggolist.refactor.place.repository.EventRepository;
import ggolist.refactor.place.repository.PopupRepository;
import ggolist.refactor.place.repository.StoreRepository;
import ggolist.refactor.user.domain.User;
import ggolist.refactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FavoriteStoreRepository favoriteStoreRepository;
    private final FavoriteEventRepository favoriteEventRepository;
    private final FavoritePopupRepository favoritePopupRepository;
    private final StoreRepository storeRepository;
    private final EventRepository eventRepository;
    private final PopupRepository popupRepository;
    private final UserRepository userRepository;

    public void addStoreFavorite(Long userId, Long storeId) {
        validateDuplicateFavorite(userId, storeId);

        User user = findUser(userId);
        Store store = findStore(storeId);

        favoriteStoreRepository.save(FavoriteStore.create(user, store));
        store.increaseLikeCount();
    }

    public void addEventFavorite(Long userId, Long eventId) {
        validateDuplicateEventFavorite(userId, eventId);

        User user = findUser(userId);
        Event event = findEvent(eventId);

        favoriteEventRepository.save(FavoriteEvent.create(user, event));
        event.increaseLikeCount();
    }

    public void addPopupFavorite(Long userId, Long popupId) {
        validateDuplicatePopupFavorite(userId, popupId);

        User user = findUser(userId);
        Popup popup = findPopup(popupId);

        favoritePopupRepository.save(FavoritePopup.create(user, popup));
        popup.increaseLikeCount();
    }

    public void deleteStoreFavorite(Long userId, Long storeId) {
        FavoriteStore favorite = findFavoriteStore(userId, storeId);
        favoriteStoreRepository.delete(favorite);
        favorite.getStore().decreaseLikeCount();
    }

    public void deleteEventFavorite(Long userId, Long eventId) {
        FavoriteEvent favorite = findFavoriteEvent(userId, eventId);

        favoriteEventRepository.delete(favorite);
        favorite.getEvent().decreaseLikeCount();
    }

    public void deletePopupFavorite(Long userId, Long popupId) {
        FavoritePopup favorite = findFavoritePopup(userId, popupId);

        favoritePopupRepository.delete(favorite);
        favorite.getPopup().decreaseLikeCount();
    }

    /******************** Helper Method ********************/
    private void validateDuplicateFavorite(Long userId, Long storeId) {
        if (favoriteStoreRepository.existsByUserIdAndStoreId(userId, storeId)) {
            throw new FavoriteDuplicateException("이미 즐겨찾기에 등록된 가게입니다.");
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));
    }

    private Store findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new PlaceNotFoundException("가게를 찾을 수 없습니다."));
    }

    private FavoriteStore findFavoriteStore(Long userId, Long storeId) {
        return favoriteStoreRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() -> new FavoriteNotFoundException("즐겨찾기 기록을 찾을 수 없습니다."));
    }

    private void validateDuplicateEventFavorite(Long userId, Long eventId) {
        if (favoriteEventRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new FavoriteDuplicateException("이미 즐겨찾기에 등록된 이벤트입니다.");
        }
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new PlaceNotFoundException("이벤트를 찾을 수 없습니다."));
    }

    private FavoriteEvent findFavoriteEvent(Long userId, Long eventId) {
        return favoriteEventRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new FavoriteNotFoundException("즐겨찾기 기록을 찾을 수 없습니다."));
    }

    private void validateDuplicatePopupFavorite(Long userId, Long popupId) {
        if (favoritePopupRepository.existsByUserIdAndPopupId(userId, popupId)) {
            throw new FavoriteDuplicateException("이미 즐겨찾기에 등록된 팝업입니다.");
        }
    }

    private Popup findPopup(Long popupId) {
        return popupRepository.findById(popupId)
                .orElseThrow(() -> new PlaceNotFoundException("팝업을 찾을 수 없습니다."));
    }

    private FavoritePopup findFavoritePopup(Long userId, Long popupId) {
        return favoritePopupRepository.findByUserIdAndPopupId(userId, popupId)
                .orElseThrow(() -> new FavoriteNotFoundException("즐겨찾기 기록을 찾을 수 없습니다."));
    }
}
