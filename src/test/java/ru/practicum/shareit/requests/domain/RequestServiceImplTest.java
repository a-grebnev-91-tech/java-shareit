package ru.practicum.shareit.requests.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.mapper.RequestMapper;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final Long OUT_USER_ID = 2L;
    private static final Long REQUEST_ID = 3L;
    @Mock
    private UserRepository userRepo;
    @Mock
    private RequestRepository requestRepo;
    @Mock
    private RequestMapper requestMapper;
    @InjectMocks
    private RequestServiceImpl service;

    @BeforeEach
    void setUp() {
        when(userRepo.existsById(USER_ID)).thenReturn(true);
    }

    @Test
    void getAllRequestsButUser() {
        service.getAllRequestsButUser(USER_ID, 1, 10);

        verify(requestRepo).findAllRequestsButUser(USER_ID, 1, 10);
        verifyNoMoreInteractions(requestRepo);

        assertThrows(NotFoundException.class, () -> service.getAllRequestsButUser(OUT_USER_ID, 1, 10));
    }

    @Test
    void getAllRequestsByUser() {
        service.getAllRequestsByUser(USER_ID);

        verify(requestRepo).findAllByRequesterId(USER_ID);
        verifyNoMoreInteractions(requestRepo);

        assertThrows(NotFoundException.class, () -> service.getAllRequestsByUser(OUT_USER_ID));
    }


    @Test
    void getRequestById() {
        when(requestRepo.findById(REQUEST_ID)).thenReturn(Optional.of(new Request()));
        service.getRequestById(USER_ID, REQUEST_ID);

        verify(requestRepo).findById(REQUEST_ID);
        verifyNoMoreInteractions(requestRepo);

        assertThrows(NotFoundException.class, () -> service.getRequestById(OUT_USER_ID, REQUEST_ID));

        assertThrows(NotFoundException.class, () -> service.getRequestById(USER_ID, 999L));
    }
}