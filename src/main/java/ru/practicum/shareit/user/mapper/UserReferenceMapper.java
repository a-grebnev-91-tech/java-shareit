package ru.practicum.shareit.user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;

@Component
public class UserReferenceMapper {
    private final UserRepository repo;

    @Autowired
    public UserReferenceMapper(UserRepository repo) {
        this.repo = repo;
    }

    public User map(@NotNull Long id) {
        return repo.getUser(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id %d isn't exist", id))
        );
    }

    public Long map(@NotNull User user) {
        return user.getId();
    }
}

