package ru.practicum.shareit.user.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exception.ConflictEmailException;
import ru.practicum.shareit.user.dao.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserRepository repository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null)
            return true;
        if (repository.getByEmail(email).isEmpty())
            return true;
        else
            throw new ConflictEmailException("Email should be unique");
    }
}
