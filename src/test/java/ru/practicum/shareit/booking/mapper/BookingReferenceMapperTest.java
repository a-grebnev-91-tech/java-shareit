package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.controller.dto.BookingForItemDto;
import ru.practicum.shareit.booking.controller.dto.ClosestBookings;
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

        when(mapper.modelToForItemDto(any())).thenAnswer(invocationOnMock -> {
            Booking booking = invocationOnMock.getArgument(0, Booking.class);
            BookingForItemDto dto = new BookingForItemDto();
            dto.setId(booking.getId());
            dto.setBookerId(booking.getBooker().getId());
            dto.setStart(booking.getStart());
            dto.setEnd(booking.getEnd());
            return dto;
        });
    }

    @Test
    void test1_shouldReturnCorrectForAllBookingsInFuture() {
        List<Booking> futureBookings = new ArrayList<>();
        Booking firstBooking = getBookingByDate(1, 1);
        Booking secondBooking = getBookingByDate(2, 2);
        futureBookings.add(firstBooking);
        futureBookings.add(secondBooking);
        when(repo.findByAvailableItem(ITEM_ID)).thenReturn(futureBookings);

        ClosestBookings dto = refMapper.itemIdToClosestBooking(ITEM_ID);
        assertNotNull(dto);
        assertNull(dto.getLastBooking());
        verify(repo).findByAvailableItem(ITEM_ID);

        BookingForItemDto forItemDto = dto.getNextBooking();
        assertNotNull(forItemDto);
        assertEquals(firstBooking.getId(), forItemDto.getId());
        assertEquals(firstBooking.getItem().getOwner().getId(), forItemDto.getBookerId());
        assertEquals(firstBooking.getStart(), forItemDto.getStart());
        assertEquals(firstBooking.getEnd(), forItemDto.getEnd());

        verify(repo).findByAvailableItem(ITEM_ID);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void test2_shouldReturnCorrectForAllBookingsInPast() {
        List<Booking> pastBookings = new ArrayList<>();
        Booking firstBooking = getBookingByDate(1, -1);
        Booking secondBooking = getBookingByDate(2, -2);
        pastBookings.add(firstBooking);
        pastBookings.add(secondBooking);
        when(repo.findByAvailableItem(ITEM_ID)).thenReturn(pastBookings);

        ClosestBookings dto = refMapper.itemIdToClosestBooking(ITEM_ID);
        assertNotNull(dto);

        assertNotNull(dto.getLastBooking());
        assertEquals(firstBooking.getId(), dto.getLastBooking().getId());
        assertEquals(firstBooking.getItem().getOwner().getId(), dto.getLastBooking().getBookerId());
        assertEquals(firstBooking.getStart(), dto.getLastBooking().getStart());
        assertEquals(firstBooking.getEnd(), dto.getLastBooking().getEnd());
        verify(repo).findByAvailableItem(ITEM_ID);

        assertNull(dto.getNextBooking());

        verify(repo).findByAvailableItem(ITEM_ID);
        verifyNoMoreInteractions(repo);
    }

    @Test
    void test3_shouldReturnCorrectForBookingInPastAndInFuture() {
        List<Booking> bookings = new ArrayList<>();
        Booking pastBooking = getBookingByDate(1, -1);
        Booking futureBooking = getBookingByDate(2, 1);
        bookings.add(pastBooking);
        bookings.add(futureBooking);
        when(repo.findByAvailableItem(ITEM_ID)).thenReturn(bookings);

        ClosestBookings dto = refMapper.itemIdToClosestBooking(ITEM_ID);
        assertNotNull(dto);

        BookingForItemDto lastDto = dto.getLastBooking();
        assertNotNull(lastDto);
        assertEquals(pastBooking.getId(), lastDto.getId());
        assertEquals(pastBooking.getItem().getOwner().getId(), lastDto.getBookerId());
        assertEquals(pastBooking.getStart(), lastDto.getStart());
        assertEquals(pastBooking.getEnd(), lastDto.getEnd());
        verify(repo).findByAvailableItem(ITEM_ID);

        BookingForItemDto nextDto = dto.getNextBooking();
        assertNotNull(nextDto);
        assertEquals(futureBooking.getId(), nextDto.getId());
        assertEquals(futureBooking.getItem().getOwner().getId(), nextDto.getBookerId());
        assertEquals(futureBooking.getStart(), nextDto.getStart());
        assertEquals(futureBooking.getEnd(), nextDto.getEnd());

        verify(repo).findByAvailableItem(ITEM_ID);
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