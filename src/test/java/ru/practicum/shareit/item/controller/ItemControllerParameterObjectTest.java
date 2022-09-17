package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingParamObj;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.domain.BookingService;
import ru.practicum.shareit.booking.domain.BookingsState;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerParameterObjectTest {
    @Mock
    BookingService service;
    @InjectMocks
    BookingController controller;
    private static final int from = 1;
    public static final int size = 10;
    public static final String state = "ALL";
    public static final Long userId = 1L;
    public static final String sortBy = "id";
    public static final  String orderBy = "ASC";

    @Test
    void test1_getAllByOwnerShouldConvertArgsToParamObj() {
        BookingParamObj paramObj = getBookingParamObj();
        when(service.getAllBookingsByOwner(paramObj)).thenReturn(null);

        controller.getAllByOwner(userId, state, from, size, sortBy, orderBy);

        ArgumentCaptor<BookingParamObj> captor = ArgumentCaptor.forClass(BookingParamObj.class);
        verify(service).getAllBookingsByOwner(captor.capture());
        BookingParamObj capturedParam = captor.getValue();

        assertNotNull(capturedParam);
        assertEquals(paramObj, capturedParam);
        assertEquals(userId, capturedParam.getUserId());
        assertEquals(BookingsState.valueOf(state), capturedParam.getState());
        assertNotNull(capturedParam.getPageable());
        assertEquals(from, capturedParam.getPageable().getOffset());
        assertEquals(size, capturedParam.getPageable().getPageSize());
        assertTrue(capturedParam.getPageable().getSort().isSorted());
        String[] sortParam = capturedParam.getPageable().getSort().toString().split(":");
        assertEquals(sortBy, sortParam[0].trim());
        assertEquals(orderBy, sortParam[1].trim());
    }

    private BookingParamObj getBookingParamObj() {
        return BookingParamObj
                .newBuilder()
                .from(from)
                .size(size)
                .withState(state)
                .withUserId(userId)
                .sortBy(sortBy)
                .sortOrder(orderBy)
                .build();
    }

    @Test
    void test2_getAllByBookerShouldConvertArgsToParamObj() {
        BookingParamObj paramObj = getBookingParamObj();
        when(service.getAllBookingsByBooker(paramObj)).thenReturn(null);

        controller.getAllByBooker(userId, state, from, size, sortBy, orderBy);

        ArgumentCaptor<BookingParamObj> captor = ArgumentCaptor.forClass(BookingParamObj.class);
        verify(service).getAllBookingsByBooker(captor.capture());
        BookingParamObj capturedParam = captor.getValue();

        assertNotNull(capturedParam);
        assertEquals(paramObj, capturedParam);
        assertEquals(userId, capturedParam.getUserId());
        assertEquals(BookingsState.valueOf(state), capturedParam.getState());
        assertNotNull(capturedParam.getPageable());
        assertEquals(from, capturedParam.getPageable().getOffset());
        assertEquals(size, capturedParam.getPageable().getPageSize());
        assertTrue(capturedParam.getPageable().getSort().isSorted());
        String[] sortParam = capturedParam.getPageable().getSort().toString().split(":");
        assertEquals(sortBy, sortParam[0].trim());
        assertEquals(orderBy, sortParam[1].trim());
    }
}