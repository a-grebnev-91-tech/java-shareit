package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> deleteById(long id);

    List<User> findAll();

    Optional<User> findByEmail(String email);

    Optional<User> findById(long id);

    User save(User user);
}

