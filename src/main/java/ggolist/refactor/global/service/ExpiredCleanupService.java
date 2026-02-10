package ggolist.refactor.global.service;

import ggolist.refactor.basePlace.repository.EventRepository;
import ggolist.refactor.basePlace.repository.PopupRepository;
import ggolist.refactor.favorite.repository.FavoriteEventRepository;
import ggolist.refactor.favorite.repository.FavoritePopupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ExpiredCleanupService {

    private final PopupRepository popupRepository;
    private final EventRepository eventRepository;
    private final FavoritePopupRepository favoritePopupRepository;
    private final FavoriteEventRepository favoriteEventRepository;

    public void cleanupExpiredPlaces() {
        LocalDate today = LocalDate.now();
        log.info("[Scheduler] 만료 데이터 벌크 삭제 시작: {}", today);

        try {
            favoritePopupRepository.deleteByExpiredPopups(today);
            int deletedPopups = popupRepository.deleteByEndDateBefore(today);
            favoriteEventRepository.deleteByExpiredEvents(today);
            int deletedEvents = eventRepository.deleteByEndDateBefore(today);

            log.info("[Scheduler] 삭제 완료 - 팝업: {}건, 이벤트: {}건", deletedPopups, deletedEvents);
        } catch (Exception e) {
            log.error("[Scheduler] 만료 데이터 삭제 중 에러 발생: {}", e.getMessage());
        }
    }
}
