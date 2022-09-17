package ru.practicum.shareit.booking.controller.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingInputValidationTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MethodSource("test1MethodSource")
    @ParameterizedTest(name = "{index}. Check valid booking dto with {4}")
    void test1_bookingValidation(
            Long id,
            LocalDateTime start,
            LocalDateTime end,
            int expectedSize,
            String testDescription
    ) {
        BookingInputDto dto = new BookingInputDto();
        dto.setItemId(id);
        dto.setStart(start);
        dto.setEnd(end);

        Set<ConstraintViolation<BookingInputDto>> violations = validator.validate(dto);

        assertEquals(expectedSize, violations.size());
    }

    Stream<Arguments> test1MethodSource() {
        LocalDateTime futureStart = LocalDateTime.now().plusDays(1);
        LocalDateTime futureEnd = LocalDateTime.now().plusDays(2);
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        Long validId = 22L;

        return Stream.of(
                Arguments.of(validId, futureStart, futureEnd, 0, "all fields is ok"),
                Arguments.of(null, futureStart, futureEnd, 1, "item id is null"),
                Arguments.of(validId, pastDate, futureEnd, 1, "start date in past"),
                Arguments.of(validId, null, futureEnd, 1, "start date is null"),
                Arguments.of(validId, futureStart, pastDate, 2, "end date in past"),
                Arguments.of(validId, futureStart, null, 1, "end date is null"),
                Arguments.of(validId, futureEnd, futureStart, 1, "start date is after end date")
        );
    }
}