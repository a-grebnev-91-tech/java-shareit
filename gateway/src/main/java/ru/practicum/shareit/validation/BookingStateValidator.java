package ru.practicum.shareit.validation;

import org.apache.commons.lang3.EnumUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingStateValidator implements ConstraintValidator<ValidBookingState, String> {
    @Override
    public boolean isValid(String stateParam, ConstraintValidatorContext constraintValidatorContext) {
        if (stateParam == null) return false;
        return EnumUtils.isValidEnum(BookingState.class, stateParam);
    }

    private static enum BookingState {
        ALL,
        CURRENT,
        PAST,
        FUTURE,
        WAITING,
        REJECTED
    }
}
