package ggolist.refactor.favorite.repository;

import ggolist.refactor.favorite.domain.FavoriteEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface FavoriteEventRepository extends JpaRepository<FavoriteEvent, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM FavoriteEvent fe WHERE fe.event.id IN " +
            "(SELECT e.id FROM Event e WHERE e.endDate < :today)")
    void deleteByExpiredEvents(@Param("today") LocalDate today);
}
