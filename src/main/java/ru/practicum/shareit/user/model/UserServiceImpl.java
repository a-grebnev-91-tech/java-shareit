package ru.practicum.shareit.user.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.validation.Valid;
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
        if (isUserExist(id)) {
            repository.deleteUser(id);
        } else {
            throw new NotFoundException(String.format("User with id %d isn't exist", id));
        }
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
    public UserDto updateUserState(long userId, UserDto user) {
        if (!isUserExist(userId)) {
            throw new NotFoundException(String.format("User with id %d isn't exist", userId));
        }
        user.setId(userId);
        if (user.getEmail() != null) {
            validateUser(user);
        }
        
        return mapper.toDto(repository.updateUser(mapper.toModel(user)));
    }

    private void validateUser(@Valid UserDto user) {
    }

    private boolean isUserExist(long id) {
        return repository.getUser(id).isPresent();
    }
}
