package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.entity.User;

import java.util.List;

public interface UserRepository {
    User createUser(User user);

    User deleteUser(long id);

    boolean emailIsAvailable(long userId, String email);

    List<User> getAll();

    User getUser(long id);

    User updateUser(User user);
}

