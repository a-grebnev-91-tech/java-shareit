package ru.practicum.shareit.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankButNullValidator implements ConstraintValidator<NotBlankButNull, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        } else {
            return !s.isBlank();
        }
    }
}
