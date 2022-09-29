package ru.practicum.shareit.util.validation;

import org.junit.jupiter.api.Test;

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