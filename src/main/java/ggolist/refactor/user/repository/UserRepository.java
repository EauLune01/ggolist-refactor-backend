package ggolist.refactor.user.repository;

import ggolist.refactor.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
