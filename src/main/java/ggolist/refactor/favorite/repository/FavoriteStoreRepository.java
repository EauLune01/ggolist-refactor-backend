package ggolist.refactor.favorite.repository;

import ggolist.refactor.favorite.domain.FavoriteStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore,Long> {
}
