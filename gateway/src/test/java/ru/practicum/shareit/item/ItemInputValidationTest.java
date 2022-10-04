package ru.practicum.shareit.item;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.validation.groups.CreateInfo;
import ru.practicum.shareit.validation.groups.PatchInfo;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemInputValidationTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @MethodSource("test1MethodSource")
    @ParameterizedTest(name = "{index}. Check valid created group item dto with {2}")
    void test1_itemCreateGroupValidation(
            ItemInputDto dto,
            int expectedSize,
            String testDescription
    ) {
        Set<ConstraintViolation<ItemInputDto>> violations = validator.validate(dto, CreateInfo.class);

        assertEquals(expectedSize, violations.size());
    }

    Stream<Arguments> test1MethodSource() {
        ItemInputDto okDto = new ItemInputDto();
        okDto.setAvailable(true);
        okDto.setName("name");
        okDto.setDescription("descr");

        ItemInputDto availableNull = new ItemInputDto();
        availableNull.setName("name");
        availableNull.setDescription("descr");

        ItemInputDto nameIsBlank = new ItemInputDto();
        nameIsBlank.setAvailable(true);
        nameIsBlank.setName(" ");
        nameIsBlank.setDescription("descr");

        ItemInputDto nameIsEmpty = new ItemInputDto();
        nameIsEmpty.setAvailable(true);
        nameIsEmpty.setName("");
        nameIsEmpty.setDescription("descr");

        ItemInputDto nameIsNull = new ItemInputDto();
        nameIsNull.setAvailable(true);
        nameIsNull.setDescription("descr");

        ItemInputDto descriptionIsBlank = new ItemInputDto();
        descriptionIsBlank.setAvailable(true);
        descriptionIsBlank.setName("name");
        descriptionIsBlank.setDescription(" ");

        ItemInputDto descriptionIsEmpty = new ItemInputDto();
        descriptionIsEmpty.setAvailable(true);
        descriptionIsEmpty.setName("name");
        descriptionIsEmpty.setDescription("");

        ItemInputDto descriptionIsNull = new ItemInputDto();
        descriptionIsNull.setAvailable(true);
        descriptionIsNull.setName("name");

        return Stream.of(
                Arguments.of(okDto, 0, " fields is ok"),
                Arguments.of(availableNull, 1, " available is null"),
                Arguments.of(nameIsBlank, 1, " name is blank"),
                Arguments.of(nameIsEmpty, 1, " name is empty"),
                Arguments.of(nameIsNull, 1, " name is null"),
                Arguments.of(descriptionIsBlank, 1, " description is blank"),
                Arguments.of(descriptionIsEmpty, 1, " description is empty"),
                Arguments.of(descriptionIsNull, 1, " description is null")
        );
    }

    @MethodSource("test2MethodSource")
    @ParameterizedTest(name = "{index}. Check valid patch group item dto with {2}")
    void test2_itemPatchGroupValidation(
            ItemInputDto dto,
            int expectedSize,
            String testDescription
    ) {
        Set<ConstraintViolation<ItemInputDto>> violations = validator.validate(dto, PatchInfo.class);

        assertEquals(expectedSize, violations.size());
    }

    Stream<Arguments> test2MethodSource() {
        ItemInputDto okDto = new ItemInputDto();
        okDto.setAvailable(true);
        okDto.setName("name");
        okDto.setDescription("descr");

        ItemInputDto availableNull = new ItemInputDto();
        availableNull.setName("name");
        availableNull.setDescription("descr");

        ItemInputDto nameIsBlank = new ItemInputDto();
        nameIsBlank.setAvailable(true);
        nameIsBlank.setName(" ");
        nameIsBlank.setDescription("descr");

        ItemInputDto nameIsEmpty = new ItemInputDto();
        nameIsEmpty.setAvailable(true);
        nameIsEmpty.setName("");
        nameIsEmpty.setDescription("descr");

        ItemInputDto nameIsNull = new ItemInputDto();
        nameIsNull.setAvailable(true);
        nameIsNull.setDescription("descr");

        ItemInputDto descriptionIsBlank = new ItemInputDto();
        descriptionIsBlank.setAvailable(true);
        descriptionIsBlank.setName("name");
        descriptionIsBlank.setDescription(" ");

        ItemInputDto descriptionIsEmpty = new ItemInputDto();
        descriptionIsEmpty.setAvailable(true);
        descriptionIsEmpty.setName("name");
        descriptionIsEmpty.setDescription("");

        ItemInputDto descriptionIsNull = new ItemInputDto();
        descriptionIsNull.setAvailable(true);
        descriptionIsNull.setName("name");

        return Stream.of(
                Arguments.of(okDto, 0, " fields is ok"),
                Arguments.of(availableNull, 0, " available is null"),
                Arguments.of(nameIsBlank, 1, " name is blank"),
                Arguments.of(nameIsEmpty, 1, " name is empty"),
                Arguments.of(nameIsNull, 0, " name is null"),
                Arguments.of(descriptionIsBlank, 1, " description is blank"),
                Arguments.of(descriptionIsEmpty, 1, " description is empty"),
                Arguments.of(descriptionIsNull, 0, " description is null")
        );
    }
}