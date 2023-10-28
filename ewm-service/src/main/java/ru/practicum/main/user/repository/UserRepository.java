package ru.practicum.main.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    List<User> getUsersByIdIn(List<Long> ids, Pageable pageable);

    void removeUserById(Long userId);

    User getUserById(Long userId);
}