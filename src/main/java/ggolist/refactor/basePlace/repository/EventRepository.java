package ggolist.refactor.basePlace.repository;

import ggolist.refactor.basePlace.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Event e WHERE e.endDate < :today")
    int deleteByEndDateBefore(@Param("today") LocalDate today);
}
