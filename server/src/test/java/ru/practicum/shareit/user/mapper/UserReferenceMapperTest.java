package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReferenceMapperTest {
    private static final Long USER_ID = 1L;
    private static final Long OUTSIDE_USER_ID = 2L;
    @Mock
    private UserRepository repo;
    @InjectMocks
    private UserReferenceMapper mapper;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
    }

    @Test
    void test1_shouldConvertIdToUser() {
        when(repo.findById(USER_ID)).thenReturn(Optional.of(user));

        User mapped = mapper.idToUser(USER_ID);
        assertNotNull(mapped);
        assertEquals(user, mapped);

        assertThrows(NotFoundException.class, () -> mapper.idToUser(OUTSIDE_USER_ID));
    }

    @Test
    void test2_shouldConvertUserToId() {
        Long mapped = mapper.userToID(user);
        assertNotNull(mapped);
        assertEquals(USER_ID, mapped);
    }
}