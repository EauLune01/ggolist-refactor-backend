package ggolist.refactor.basePlace.repository;

import ggolist.refactor.basePlace.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
