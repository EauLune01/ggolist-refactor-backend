package ggolist.refactor.place.repository;

import ggolist.refactor.place.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
