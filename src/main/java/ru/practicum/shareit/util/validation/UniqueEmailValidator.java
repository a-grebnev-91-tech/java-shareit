package ru.practicum.shareit.util.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.practicum.shareit.exception.ConflictEmailException;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Qualifier("InDbUsers")
    private final UserRepository repository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null)
            return true;
        if (repository.findByEmail(email).isEmpty())
            return true;
        else
            throw new ConflictEmailException("Email should be unique");
    }
}
