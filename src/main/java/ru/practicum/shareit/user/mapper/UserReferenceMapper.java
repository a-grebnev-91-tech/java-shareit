package ru.practicum.shareit.user.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.domain.User;

@Component
@RequiredArgsConstructor
public class UserReferenceMapper {
    @Qualifier("InDbUsers")
    private final UserRepository repo;

    public User idToUser(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id %d isn't exist", id))
        );
    }

    public Long userToID(User user) {
        return user.getId();
    }
}

