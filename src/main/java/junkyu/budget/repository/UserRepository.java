package junkyu.budget.repository;

import junkyu.budget.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccount(String account);

    List<User> findAllByNotification(Boolean notification);
}
