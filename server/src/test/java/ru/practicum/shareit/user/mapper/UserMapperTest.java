package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserMapperTest {
    private UserMapper mapper = Mappers.getMapper(UserMapper.class);
    private User model;
    private UserDto dto;

    @BeforeAll
    void beforeAll() {
        model = new User();
        model.setId(1L);
        model.setName("name");
        model.setEmail("mail@mail.ru");

        dto = new UserDto();
        dto.setId(1L);
        dto.setName("name");
        dto.setEmail("mail@mail.ru");
    }

    @Test
    void test1_toDto() {
        UserDto fromMapper = mapper.toDto(model);

        assertNotNull(fromMapper);
        assertEquals(model.getId(), fromMapper.getId());
        assertEquals(model.getName(), fromMapper.getName());
        assertEquals(model.getEmail(), fromMapper.getEmail());
    }

    @Test
    void test2_toModel() {
        User fromMapper = mapper.toModel(dto);

        assertNotNull(fromMapper);
        assertEquals(dto.getId(), fromMapper.getId());
        assertEquals(dto.getName(), fromMapper.getName());
        assertEquals(dto.getEmail(), fromMapper.getEmail());
    }

    @Test
    void test3_toModel() {
        User fromMapper = mapper.toModel(1L);

        assertNotNull(fromMapper);
        assertEquals(1, fromMapper.getId());
    }
}