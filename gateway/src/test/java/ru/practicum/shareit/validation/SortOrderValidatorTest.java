package ru.practicum.shareit.validation;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.validation.SortOrderValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SortOrderValidatorTest {
    @Test
    void test1_isValid() {
        SortOrderValidator validator = new SortOrderValidator();
        String validAsc = "ASC";
        String validDesc = "DESC";
        String invalid = "ololo";

        assertTrue(validator.isValid(validAsc, null));
        assertTrue(validator.isValid(validDesc, null));
        assertFalse(validator.isValid(invalid, null));
    }

}