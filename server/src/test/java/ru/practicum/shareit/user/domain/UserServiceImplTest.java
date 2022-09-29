package ru.practicum.shareit.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.Patcher;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "name";
    private static final String USER_NAME = "mail@mail.ru";
    @Mock
    private UserRepository repo;
    @Mock
    private UserMapper mapper;
    @Mock
    private Patcher patcher;
    @InjectMocks
    private UserServiceImpl service;
    private User user;
    private UserDto inputDto;
    private UserDto outputDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        user.setEmail(USER_EMAIL);
        inputDto = new UserDto();
        inputDto.setName(USER_NAME);
        inputDto.setEmail(USER_EMAIL);
        outputDto = new UserDto();
        outputDto.setId(USER_ID);
        outputDto.setName(USER_NAME);
        outputDto.setEmail(USER_EMAIL);

        when(repo.findById(USER_ID)).thenReturn(Optional.of(user));
        when(repo.findAll()).thenReturn(List.of(user));
        when(repo.save(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(outputDto);
        when(mapper.toModel(USER_ID)).thenReturn(user);
        when(mapper.toModel(inputDto)).thenReturn(user);
        when(patcher.patch(any(), any())).thenReturn(true);
    }

    @Test
    void deleteUser() {
        assertDoesNotThrow(() -> service.deleteUser(USER_ID));
        assertThrows(NotFoundException.class, () -> service.deleteUser(999L));
    }

    @Test
    void getAll() {
        List<UserDto> dtos = service.getAll();
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertNotNull(dtos.get(0));
        assertEquals(outputDto, dtos.get(0));
    }

    @Test
    void getUser() {
        UserDto fromRepo = service.getUser(USER_ID);
        assertNotNull(fromRepo);
        assertEquals(outputDto, fromRepo);

        assertThrows(NotFoundException.class, () -> service.getUser(999L));
    }

    @Test
    void patchUser() {
        UserDto fromRepo = service.patchUser(USER_ID, inputDto);
        assertNotNull(fromRepo);
        assertEquals(outputDto, fromRepo);
    }
}