package ru.practicum.shareit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = BookingStateValidator.class)
public @interface ValidBookingState {
    String message() default "Booking state should be one of: ALL, CURRENT, FUTURE, PAST, REJECTED, WAITING;";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
