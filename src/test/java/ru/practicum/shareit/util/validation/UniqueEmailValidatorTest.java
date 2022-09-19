package ru.practicum.shareit.util.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConflictEmailException;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniqueEmailValidatorTest {
    private static final String CONFLICT_EMAIL = "mail@mail.com";
    private static final String UNIQUE_EMAIL = "unique@mail.com";
    @Mock
    private UserRepository repo;
    @InjectMocks
    private UniqueEmailValidator validator;

    @Test
    void isValid() {
        when(repo.findByEmail(CONFLICT_EMAIL)).thenReturn(Optional.of(new User()));

        assertThrows(ConflictEmailException.class, () -> validator.isValid(CONFLICT_EMAIL, null));
        assertTrue(validator.isValid(UNIQUE_EMAIL, null));
    }
}