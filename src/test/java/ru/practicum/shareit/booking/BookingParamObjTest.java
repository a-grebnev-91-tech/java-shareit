package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.BookingStateIsNotSupportedException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.BookingParamObj.*;

class BookingParamObjTest {

    @Test
    void test1_shouldReturnValidEmptyBuilder() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().build();
        Sort sort = Sort.by(Sort.Direction.valueOf(BOOKING_DEFAULT_ORDER), BOOKING_DEFAULT_SORT_BY);

        assertNull(paramObj.getUserId());

        assertNotNull(paramObj.getState());

        assertEquals(BOOKING_DEFAULT_STATE, paramObj.getState().name());

        Pageable pageable = paramObj.getPageable();
        assertNotNull(pageable);
        assertEquals(BOOKING_DEFAULT_OFFSET, pageable.getOffset());
        assertEquals(BOOKING_DEFAULT_SIZE, pageable.getPageSize());

        Sort sortFromPageable = pageable.getSort();
        assertNotNull(sortFromPageable);
        assertEquals(sort, sortFromPageable);
    }

    @Test
    void test2_shouldReturnValidBuilder() {
        Long id = 1L;
        String state = "WAITING";
        String sortBy = "end";
        String orderBy = "ASC";
        int size = 1;
        int offset = 0;
        BookingParamObj paramObj =
                BookingParamObj
                        .newBuilder()
                        .withUserId(id)
                        .withState(state)
                        .sortBy(sortBy)
                        .sortOrder(orderBy)
                        .size(size)
                        .from(offset)
                        .build();
        Sort sort = Sort.by(Sort.Direction.valueOf(orderBy), sortBy);

        assertNotNull(paramObj.getUserId());
        assertEquals(id, paramObj.getUserId());

        assertNotNull(paramObj.getState());
        assertEquals(state, paramObj.getState().name());

        Pageable pageable = paramObj.getPageable();
        assertNotNull(pageable);
        assertEquals(offset, pageable.getOffset());
        assertEquals(size, pageable.getPageSize());

        Sort sortFromPageable = pageable.getSort();
        assertNotNull(sortFromPageable);
        assertEquals(sort, sortFromPageable);
    }

    @Test
    void test3_shouldThrowForInvalidState() {
        String state = "invalid";
        assertThrows(
                BookingStateIsNotSupportedException.class, () -> BookingParamObj.newBuilder().withState(state).build());
    }

    @Test
    void test4_shouldThrowForInvalidOrder() {
        String order = "order";
        assertThrows(
                IllegalArgumentException.class, () -> BookingParamObj.newBuilder().sortOrder(order).build());
    }

    @Test
    void test5_shouldThrowForInvalidSize() {
        int size = 0;
        assertThrows(
                IllegalArgumentException.class, () -> BookingParamObj.newBuilder().size(size).build());
    }

    @Test
    void test6_shouldThrowForInvalidFrom() {
        int from = -1;
        assertThrows(
                IllegalArgumentException.class, () -> BookingParamObj.newBuilder().from(from).build());
    }
}