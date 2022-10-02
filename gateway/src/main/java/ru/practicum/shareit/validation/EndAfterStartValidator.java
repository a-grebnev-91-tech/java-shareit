package ru.practicum.shareit.validation;

import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EndAfterStartValidator implements ConstraintValidator<EndAfterStart, BookingInputDto> {
    @Override
    public boolean isValid(BookingInputDto bookingInputDto, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingInputDto == null)
            return true;
        LocalDateTime start = bookingInputDto.getStart();
        LocalDateTime end = bookingInputDto.getEnd();
        if (start == null || end == null)
            return false;
        return end.isAfter(start);
    }
}
