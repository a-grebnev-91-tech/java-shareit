package ru.practicum.shareit.item.controller.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentInputValidationTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MethodSource("test1MethodSource")
    @ParameterizedTest(name = "{index}. Check valid booking dto with {2}")
    void test1_commentValidation(
            String text,
            int expectedSize,
            String testDescription
    ) {
        CommentInputDto dto = new CommentInputDto();
        dto.setText(text);

        Set<ConstraintViolation<CommentInputDto>> violations = validator.validate(dto);

        assertEquals(expectedSize, violations.size());
    }

    Stream<Arguments> test1MethodSource() {
        return Stream.of(
                Arguments.of("text", 0, " content is ok"),
                Arguments.of("t", 0, " content is ok"),
                Arguments.of(" ", 1, " content is blank"),
                Arguments.of("", 1, " content is empty"),
                Arguments.of(null, 1, " content is null")
        );
    }
}
