package ggolist.refactor.place.repository;

import ggolist.refactor.place.domain.Event;
import ggolist.refactor.place.repository.custom.EventRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Event e WHERE e.endDate < :today")
    int deleteByEndDateBefore(@Param("today") LocalDate today);
}
