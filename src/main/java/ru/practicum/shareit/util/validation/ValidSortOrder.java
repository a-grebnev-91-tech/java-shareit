package ru.practicum.shareit.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SortOrderValidator.class })
public @interface ValidSortOrder {

        String message() default "Sort order should be one of ASC or DESC";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
}
