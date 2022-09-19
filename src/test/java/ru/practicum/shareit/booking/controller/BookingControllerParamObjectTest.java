package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingParamObj;
import ru.practicum.shareit.booking.domain.BookingService;
import ru.practicum.shareit.booking.domain.BookingsState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingControllerParamObjectTest {
    private static final int FROM = 1;
    private static final int SIZE = 10;
    private static final String STATE = "ALL";
    private static final Long USER_ID = 1L;
    private static final Long BOOKING_ID = 2L;
    private static final String SORT_BY = "id";
    private static final String ORDER_BY = "ASC";
    private static final LocalDateTime START = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime END = LocalDateTime.now().plusDays(2);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    @Mock
    private BookingService service;
    @InjectMocks
    private BookingController controller;

    @Test
    void test1_getAllByOwnerShouldConvertArgsToParamObj() {
        BookingParamObj paramObj = getBookingParamObj();
        when(service.getAllBookingsByOwner(paramObj)).thenReturn(null);

        controller.getAllByOwner(USER_ID, STATE, FROM, SIZE, SORT_BY, ORDER_BY);

        ArgumentCaptor<BookingParamObj> captor = ArgumentCaptor.forClass(BookingParamObj.class);
        verify(service).getAllBookingsByOwner(captor.capture());
        BookingParamObj capturedParam = captor.getValue();

        assertNotNull(capturedParam);
        assertEquals(paramObj, capturedParam);
        assertEquals(USER_ID, capturedParam.getUserId());
        assertEquals(BookingsState.valueOf(STATE), capturedParam.getState());
        assertNotNull(capturedParam.getPageable());
        assertEquals(FROM, capturedParam.getPageable().getOffset());
        assertEquals(SIZE, capturedParam.getPageable().getPageSize());
        assertTrue(capturedParam.getPageable().getSort().isSorted());
        String[] sortParam = capturedParam.getPageable().getSort().toString().split(":");
        assertEquals(SORT_BY, sortParam[0].trim());
        assertEquals(ORDER_BY, sortParam[1].trim());
    }

    @Test
    void test2_getAllByBookerShouldConvertArgsToParamObj() {
        BookingParamObj paramObj = getBookingParamObj();
        when(service.getAllBookingsByBooker(paramObj)).thenReturn(null);

        controller.getAllByBooker(USER_ID, STATE, FROM, SIZE, SORT_BY, ORDER_BY);

        ArgumentCaptor<BookingParamObj> captor = ArgumentCaptor.forClass(BookingParamObj.class);
        verify(service).getAllBookingsByBooker(captor.capture());
        BookingParamObj capturedParam = captor.getValue();

        assertNotNull(capturedParam);
        assertEquals(paramObj, capturedParam);
        assertEquals(USER_ID, capturedParam.getUserId());
        assertEquals(BookingsState.valueOf(STATE), capturedParam.getState());
        assertNotNull(capturedParam.getPageable());
        assertEquals(FROM, capturedParam.getPageable().getOffset());
        assertEquals(SIZE, capturedParam.getPageable().getPageSize());
        assertTrue(capturedParam.getPageable().getSort().isSorted());
        String[] sortParam = capturedParam.getPageable().getSort().toString().split(":");
        assertEquals(SORT_BY, sortParam[0].trim());
        assertEquals(ORDER_BY, sortParam[1].trim());
    }


    private BookingParamObj getBookingParamObj() {
        return BookingParamObj
                .newBuilder()
                .from(FROM)
                .size(SIZE)
                .withState(STATE)
                .withUserId(USER_ID)
                .sortBy(SORT_BY)
                .sortOrder(ORDER_BY)
                .build();
    }
}
