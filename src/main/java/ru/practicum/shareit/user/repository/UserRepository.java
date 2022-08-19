package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User createUser(User user);

    Optional<User> deleteUser(long id);

    Optional<User> getByEmail(String email);

    List<User> getAll();

    Optional<User> getUser(long id);

    User updateUser(User user);
}

