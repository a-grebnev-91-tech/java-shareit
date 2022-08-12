package ru.practicum.shareit.user.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Optional;

//todo del
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class UserDtoValidation {
    private final UserRepository repository;

    public void validateUser(@Valid UserDto user){
        String email = user.getEmail();
        if (email == null) {
            throw new ValidationException("Email couldn't be null");
        }
        Optional<User> existingUser = repository.getByEmail(email);
        if (existingUser.isPresent())
            if (user.getId() == null || user.getId() != existingUser.get().getId())
                throw new ValidationException(String.format("Email %s already exist", email));
    }
}
