package ru.practicum.shareit.user.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PatchException;
import ru.practicum.shareit.user.controller.UserPatchDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.util.Patcher;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final Patcher patcher;


    @Override
    public UserDto createUser(UserDto user) {
        return userMapper.toDto(repository.createUser(userMapper.toModel(user)));
    }

    @Override
    public void deleteUser(long id) {
        getUserOrThrow(id);
        repository.deleteUser(id);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = repository.getAll();
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long id) {
        Optional<User> user = repository.getUser(id);
        return userMapper.toDto(
                user.orElseThrow(
                        () -> new NotFoundException(String.format("User with id %d isn't exist", id))
                )
        );
    }

    @Override
    public UserDto patchUser(long userId, UserPatchDto patch) {
        User existingUser = getUserOrThrow(userId);
        if (patcher.patch(existingUser, patch)) {
            return userMapper.toDto(repository.updateUser(existingUser));
        } else {
            throw new PatchException(String.format("Patch %s couldn't be applied on %s", patch, existingUser));
        }
    }

    private User getUserOrThrow(long userId) {
        Optional<User> user = repository.getUser(userId);
        return user.orElseThrow(() -> new NotFoundException(String.format("User with id %d isn't exist", userId)));
    }
}