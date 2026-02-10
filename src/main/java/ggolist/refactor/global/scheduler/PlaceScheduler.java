package ggolist.refactor.global.scheduler;

import ggolist.refactor.global.service.ExpiredCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceScheduler {

    private final ExpiredCleanupService expiredCleanupService;

    @Scheduled(cron = "0 0 0 * * *")
    public void runCleanup() {
        expiredCleanupService.cleanupExpiredPlaces();
    }
}
