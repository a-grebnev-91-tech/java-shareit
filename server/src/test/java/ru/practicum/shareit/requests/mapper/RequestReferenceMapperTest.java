package ru.practicum.shareit.requests.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.requests.domain.Request;
import ru.practicum.shareit.requests.repository.RequestRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestReferenceMapperTest {
    private static final Long REQUEST_ID = 1L;
    @Mock
    private RequestRepository repo;
    @InjectMocks
    private RequestReferenceMapper mapper;
    private Request request;

    @BeforeEach
    void setUp() {
        request = new Request();
        request.setId(REQUEST_ID);
    }

    @Test
    void test1_idToRequest() {
        Request nullRequest = mapper.idToRequest(null);
        assertNull(nullRequest);

        when(repo.findById(REQUEST_ID)).thenReturn(Optional.of(request));
        Request fromMapper = mapper.idToRequest(REQUEST_ID);
        assertEquals(REQUEST_ID, fromMapper.getId());
    }

    @Test
    void test2_requestToId() {
        Long fromMapper = mapper.requestToId(request);
        assertNotNull(fromMapper);
        assertEquals(REQUEST_ID, fromMapper);
    }
}