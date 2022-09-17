package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.controller.dto.BookingForItemDto;
import ru.practicum.shareit.booking.controller.dto.LastBookingDto;
import ru.practicum.shareit.booking.controller.dto.NextBookingDto;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingReferenceMapperTest {
    private static final Long ITEM_ID = 1L;
    private static final Long USER_ID = 2L;

    private Item item;
    private User user;

    @Mock
    private BookingRepository repo;
    @Mock
    private BookingMapper mapper;
    @InjectMocks
    private BookingReferenceMapper refMapper;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);

        item = new Item();
        item.setId(ITEM_ID);
        item.setOwner(user);

        doAnswer(invocationOnMock -> {
            Booking booking = invocationOnMock.getArgument(0, Booking.class);
            BookingForItemDto dto = invocationOnMock.getArgument(1, BookingForItemDto.class);
            dto.setId(booking.getId());
            dto.setBookerId(booking.getBooker().getId());
            dto.setStart(booking.getStart());
            dto.setEnd(booking.getEnd());
            return null;
        }).when(mapper).updateBookingForItemFromBooking(any(), any());
    }

    @Test
    void test1_shouldReturnCorrectForAllBookingsInFuture() {
        List<Booking> futureBookings = new ArrayList<>();
        Booking firstBooking = getBookingByDate(1, 1);
        Booking secondBooking = getBookingByDate(2, 2);
        futureBookings.add(firstBooking);
        futureBookings.add(secondBooking);
        when(repo.findByItemId(ITEM_ID)).thenReturn(futureBookings);

        LastBookingDto nullDto = refMapper.mapToLast(ITEM_ID);
        assertNull(nullDto);
        verify(repo).findByItemId(ITEM_ID);

        NextBookingDto dto = refMapper.mapToNext(ITEM_ID);
        assertNotNull(dto);
        assertEquals(firstBooking.getId(), dto.getId());
        assertEquals(firstBooking.getItem().getOwner().getId(), dto.getBookerId());
        assertEquals(firstBooking.getStart(), dto.getStart());
        assertEquals(firstBooking.getEnd(), dto.getEnd());

        verify(repo, times(2)).findByItemId(ITEM_ID);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void test2_shouldReturnCorrectForAllBookingsInPast() {
        List<Booking> pastBookings = new ArrayList<>();
        Booking firstBooking = getBookingByDate(1, -1);
        Booking secondBooking = getBookingByDate(2, -2);
        pastBookings.add(firstBooking);
        pastBookings.add(secondBooking);
        when(repo.findByItemId(ITEM_ID)).thenReturn(pastBookings);

        LastBookingDto dto = refMapper.mapToLast(ITEM_ID);
        assertNotNull(dto);
        assertEquals(firstBooking.getId(), dto.getId());
        assertEquals(firstBooking.getItem().getOwner().getId(), dto.getBookerId());
        assertEquals(firstBooking.getStart(), dto.getStart());
        assertEquals(firstBooking.getEnd(), dto.getEnd());
        verify(repo).findByItemId(ITEM_ID);

        NextBookingDto nullDto = refMapper.mapToNext(ITEM_ID);
        assertNull(nullDto);

        verify(repo, times(2)).findByItemId(ITEM_ID);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void test3_shouldReturnCorrectForBookingInPastAndInFuture() {
        List<Booking> bookings = new ArrayList<>();
        Booking pastBooking = getBookingByDate(1, -1);
        Booking futureBooking = getBookingByDate(2, 1);
        bookings.add(pastBooking);
        bookings.add(futureBooking);
        when(repo.findByItemId(ITEM_ID)).thenReturn(bookings);

        LastBookingDto lastDto = refMapper.mapToLast(ITEM_ID);
        assertNotNull(lastDto);
        assertEquals(pastBooking.getId(), lastDto.getId());
        assertEquals(pastBooking.getItem().getOwner().getId(), lastDto.getBookerId());
        assertEquals(pastBooking.getStart(), lastDto.getStart());
        assertEquals(pastBooking.getEnd(), lastDto.getEnd());
        verify(repo).findByItemId(ITEM_ID);

        NextBookingDto nextDto = refMapper.mapToNext(ITEM_ID);
        assertNotNull(nextDto);
        assertEquals(futureBooking.getId(), nextDto.getId());
        assertEquals(futureBooking.getItem().getOwner().getId(), nextDto.getBookerId());
        assertEquals(futureBooking.getStart(), nextDto.getStart());
        assertEquals(futureBooking.getEnd(), nextDto.getEnd());

        verify(repo, times(2)).findByItemId(ITEM_ID);
        verifyNoMoreInteractions(repo);
    }

    private Booking getBookingByDate(long id, int daysOffset) {
        LocalDateTime start = LocalDateTime.now().plusDays(daysOffset);
        LocalDateTime end = start.plusHours(1);
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setId(id);
        booking.setStart(start);
        booking.setEnd(end);
        return booking;
    }
}