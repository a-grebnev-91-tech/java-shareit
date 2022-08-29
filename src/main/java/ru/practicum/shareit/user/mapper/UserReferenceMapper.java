package ru.practicum.shareit.user.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class UserReferenceMapper {
    @Qualifier("InMemory")
    private final UserRepository repo;

    public User map(@NotNull Long id) {
        return repo.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id %d isn't exist", id))
        );
    }

    public Long map(@NotNull User user) {
        return user.getId();
    }
}

