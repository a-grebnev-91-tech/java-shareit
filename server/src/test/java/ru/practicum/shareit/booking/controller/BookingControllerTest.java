package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.dto.BookingInputDto;
import ru.practicum.shareit.booking.controller.dto.BookingOutputDto;
import ru.practicum.shareit.booking.domain.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@WebMvcTest(controllers = BookingController.class)
@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    private static final Long USER_ID = 1L;
    private static final Long BOOKING_ID = 2L;
    private static final LocalDateTime START = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime END = LocalDateTime.now().plusDays(2);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    @MockBean
    private BookingService service;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private BookingInputDto inDto;
    private BookingOutputDto outDto;

    @BeforeEach
    void setUp() {
        inDto = new BookingInputDto();
        inDto.setItemId(1L);
        inDto.setStart(START);
        inDto.setEnd(END);
        outDto = new BookingOutputDto();
        outDto.setId(BOOKING_ID);
        outDto.setStart(START);
        outDto.setEnd(END);
    }

    @Test
    void test1_createBooking() throws Exception {
        when(service.createBooking(any(), any()))
                .thenReturn(outDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(inDto))
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", startsWith(START.format(FORMATTER)), String.class))
                .andExpect(jsonPath("$.end", startsWith(END.format(FORMATTER)), String.class));
    }

    @Test
    void test2_getBooking() throws Exception {
        when(service.getBooking(any(), any()))
                .thenReturn(outDto);

        mvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", startsWith(START.format(FORMATTER)), String.class))
                .andExpect(jsonPath("$.end", startsWith(END.format(FORMATTER)), String.class));
    }

    @Test
    void test3_approveBooking() throws Exception {
        when(service.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(outDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header(USER_ID_HEADER, USER_ID)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(outDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", startsWith(START.format(FORMATTER)), String.class))
                .andExpect(jsonPath("$.end", startsWith(END.format(FORMATTER)), String.class));
    }
}