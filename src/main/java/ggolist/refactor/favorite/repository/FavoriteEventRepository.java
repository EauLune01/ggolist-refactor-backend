package ggolist.refactor.favorite.repository;

import ggolist.refactor.favorite.domain.FavoriteEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface FavoriteEventRepository extends JpaRepository<FavoriteEvent, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM FavoriteEvent fe WHERE fe.event.id IN " +
            "(SELECT e.id FROM Event e WHERE e.endDate < :today)")
    void deleteByExpiredEvents(@Param("today") LocalDate today);

    @Query("select f.event.id from FavoriteEvent f where f.user.id = :userId")
    Set<Long> findEventIdsByUserId(@Param("userId") Long userId);

    @Query("select f from FavoriteEvent f join fetch f.event where f.user.id = :userId and f.event.id = :eventId")
    Optional<FavoriteEvent> findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    boolean existsByUserIdAndEventId(Long userId, Long eventId);
}
