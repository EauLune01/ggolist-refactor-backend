package ggolist.refactor.favorite.repository;

import ggolist.refactor.favorite.domain.FavoritePopup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface FavoritePopupRepository extends JpaRepository<FavoritePopup, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM FavoritePopup fp WHERE fp.popup.id IN " +
            "(SELECT p.id FROM Popup p WHERE p.endDate < :today)")
    void deleteByExpiredPopups(@Param("today") LocalDate today);
}
