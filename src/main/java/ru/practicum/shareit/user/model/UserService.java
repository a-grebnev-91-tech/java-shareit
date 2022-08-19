package ru.practicum.shareit.user.model;

import ru.practicum.shareit.user.controller.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);

    void deleteUser(long id);

    List<UserDto> getAll();

    UserDto getUser(long id);

    UserDto patchUser(long userId, UserDto user);
}
