package ggolist.refactor.favorite.repository;

import ggolist.refactor.favorite.domain.FavoritePopup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface FavoritePopupRepository extends JpaRepository<FavoritePopup, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM FavoritePopup fp WHERE fp.popup.id IN " +
            "(SELECT p.id FROM Popup p WHERE p.endDate < :today)")
    void deleteByExpiredPopups(@Param("today") LocalDate today);

    @Query("select f.popup.id from FavoritePopup f where f.user.id = :userId")
    Set<Long> findPopupIdsByUserId(@Param("userId") Long userId);

    @Query("select f from FavoritePopup f join fetch f.popup where f.user.id = :userId and f.popup.id = :popupId")
    Optional<FavoritePopup> findByUserIdAndPopupId(@Param("userId") Long userId, @Param("popupId") Long popupId);

    boolean existsByUserIdAndPopupId(Long userId, Long popupId);
}
