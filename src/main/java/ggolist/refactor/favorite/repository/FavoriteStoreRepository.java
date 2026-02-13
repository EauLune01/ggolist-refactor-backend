package ggolist.refactor.favorite.repository;

import ggolist.refactor.favorite.domain.FavoriteStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore,Long> {

    @Query("select f.store.id from FavoriteStore f where f.user.id = :userId")
    Set<Long> findStoreIdsByUserId(@Param("userId") Long userId);

    @Query("select f from FavoriteStore f join fetch f.store where f.user.id = :userId and f.store.id = :storeId")
    Optional<FavoriteStore> findByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
