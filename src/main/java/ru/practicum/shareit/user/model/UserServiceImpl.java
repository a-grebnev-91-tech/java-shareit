package ru.practicum.shareit.user.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.controller.UserPatchDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto createUser(UserDto user) {
        return mapper.toDto(repository.createUser(mapper.toModel(user)));
    }

    @Override
    public void deleteUser(long id) {
        throwExceptionIfNotExist(id);
        repository.deleteUser(id);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = repository.getAll();
        return users.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long id) {
        Optional<User> user = repository.getUser(id);
        return mapper.toDto(
                user.orElseThrow(
                        () -> new NotFoundException(String.format("User with id %d isn't exist", id))
                )
        );
    }

    @Override
    public UserDto updateUserState(long userId, UserPatchDto patch) {
        throwExceptionIfNotExist(userId);
        User existingUser = repository.getUser(userId).get();
        Arrays.stream(patch.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> {
                    try {
                        return field.get(patch) != null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList())
                .forEach(fieldToPatch -> {
                    try {
                        Field existingUserField = existingUser.getClass().getDeclaredField(fieldToPatch.getName());
                        existingUserField.setAccessible(true);
                        existingUserField.set(existingUser, fieldToPatch.get(patch));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        return mapper.toDto(repository.updateUser(existingUser));
    }

    private void throwExceptionIfNotExist(long id) {
        if (repository.getUser(id).isEmpty()) {
            throw new NotFoundException(String.format("User with id %d isn't exist", id));
        }
    }
}