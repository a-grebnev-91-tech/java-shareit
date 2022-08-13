package ru.practicum.shareit.user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class UserReferenceMapper {
    private final UserRepository repo;

    @Autowired
    public UserReferenceMapper(UserRepository repo) {
        this.repo = repo;
    }

    public User map(@NotNull Long id) {
        Optional<User> user = repo.getUser(id);
        if (user.isEmpty())
            throw new NotFoundException(String.format("User with id %d isn't exist", id));
        return user.get();
    }

    public Long map(@NotNull User user) {
        return user.getId();
    }
}

