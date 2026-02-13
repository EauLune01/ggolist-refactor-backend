package ggolist.refactor.place.repository;

import ggolist.refactor.place.domain.Popup;
import ggolist.refactor.place.repository.custom.PopupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PopupRepository extends JpaRepository<Popup, Long>, PopupRepositoryCustom {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Popup p WHERE p.endDate < :today")
    int deleteByEndDateBefore(@Param("today") LocalDate today);
}
