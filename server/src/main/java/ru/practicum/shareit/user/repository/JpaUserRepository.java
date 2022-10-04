package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.domain.User;

@Repository("InDbUsers")
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
}
