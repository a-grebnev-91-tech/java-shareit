package ru.practicum.shareit.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SortOrderValidator implements ConstraintValidator<ValidSortOrder, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return false;
        return s.equalsIgnoreCase("ASC") || s.equalsIgnoreCase("DESC");
    }
}
