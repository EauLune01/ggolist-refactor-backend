package ggolist.refactor.basePlace.repository;

import ggolist.refactor.basePlace.domain.Popup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PopupRepository extends JpaRepository<Popup, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Popup p WHERE p.endDate < :today")
    int deleteByEndDateBefore(@Param("today") LocalDate today);
}
