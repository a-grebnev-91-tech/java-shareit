package ru.practicum.shareit.util.validation;

import ru.practicum.shareit.booking.controller.dto.BookingRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, BookingRequest> {
    @Override
    public boolean isValid(BookingRequest bookingRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingRequest == null)
            return true;
        LocalDateTime start = bookingRequest.getStart();
        LocalDateTime end = bookingRequest.getEnd();
        if (start == null || end == null)
            return true;
        return end.isAfter(start);
    }
}
