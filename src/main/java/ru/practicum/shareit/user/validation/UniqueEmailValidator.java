package ru.practicum.shareit.user.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.controller.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, UserDto> {
    private final UserRepository repository;

    @Override
    public boolean isValid(UserDto dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto == null) {
            throw new IllegalArgumentException("Could not validate null dto");
        }
        return repository.emailIsAvailable(dto.getId(), dto.getEmail());
    }
}
