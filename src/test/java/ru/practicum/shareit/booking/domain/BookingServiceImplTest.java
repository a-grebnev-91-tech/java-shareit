package ru.practicum.shareit.booking.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.domain.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.domain.BookingStatus.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceImplTest {
    private static final Long OWNER_ID = 1L;
    private static final Long BOOKER_ID = 2L;
    private static final Long OUTSIDE_USER_ID = 3L;
    private static final Long ITEM_ID = 4L;
    private static final Long BOOKING_ID = 5L;

    Booking book;

    @Mock
    UserRepository userRepo;
    @Mock
    BookingRepository bookingRepo;
    @Mock
    BookingMapper mapper;
    @InjectMocks
    BookingServiceImpl service;

    @BeforeEach
    void setUp() {
        book = getBooking();
        when(userRepo.existsById(OWNER_ID)).thenReturn(true);
        when(userRepo.findById(OWNER_ID)).thenReturn(Optional.of(book.getItem().getOwner()));

        when(userRepo.existsById(BOOKER_ID)).thenReturn(true);
        when(userRepo.findById(BOOKER_ID)).thenReturn(Optional.of(book.getBooker()));
        when(bookingRepo.findById(BOOKING_ID)).thenReturn(Optional.of(book));

        User outside = new User();
        outside.setId(OUTSIDE_USER_ID);
        when(userRepo.existsById(OUTSIDE_USER_ID)).thenReturn(true);
        when(userRepo.findById(OUTSIDE_USER_ID)).thenReturn(Optional.of(outside));
    }

    @Test
    void test1_notExistingUserAttemptToApproveBooking() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.approveBooking(BOOKING_ID, 999L, true)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test2_outsideUserAttemptToApproveBooking() {
        Throwable throwable = assertThrows(
                NotAvailableException.class,
                () -> service.approveBooking(BOOKING_ID, OUTSIDE_USER_ID, true)
        );
        assertNotNull(throwable.getMessage());
    }

    //TODO исправить после завершения проекта на NotAvailable
    @Test
    void test3_BookerAttemptToApproveBooking() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.approveBooking(BOOKING_ID, BOOKER_ID, true)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test4_nullUserAttemptToApproveBooking() {
        Throwable throwable = assertThrows(
                NotAvailableException.class,
                () -> service.approveBooking(BOOKING_ID, null, true)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test5_ownerAttemptToApproveNonExistingBooking() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.approveBooking(999L, OWNER_ID, true)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test6_ownerAttemptToApproveNullBooking() {
        Throwable throwable = assertThrows(
                NotAvailableException.class,
                () -> service.approveBooking(null, OWNER_ID, true)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test7_ownerAttemptToApproveNotWaitingBooking() {
        book.setStatus(APPROVED);
        Throwable approvedThrow = assertThrows(
                NotAvailableException.class,
                () -> service.approveBooking(BOOKING_ID, OWNER_ID, true)
        );
        assertNotNull(approvedThrow.getMessage());

        book.setStatus(REJECTED);
        Throwable rejectedThrow = assertThrows(
                NotAvailableException.class,
                () -> service.approveBooking(BOOKING_ID, OWNER_ID, true)
        );
        assertNotNull(rejectedThrow.getMessage());

        book.setStatus(CANCELED);
        Throwable canceledThrow = assertThrows(
                NotAvailableException.class,
                () -> service.approveBooking(BOOKING_ID, OWNER_ID, true)
        );
        assertNotNull(canceledThrow.getMessage());
    }

    @Test
    void test8_ownerApproveBooking() {
        assertDoesNotThrow(() -> service.approveBooking(BOOKING_ID, OWNER_ID, true));

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepo).save(captor.capture());
        Booking captBook = captor.getValue();
        assertNotNull(captBook);
        assertEquals(APPROVED, captBook.getStatus());
    }

    @Test
    void test9_ownerRejectBooking() {
        assertDoesNotThrow(() -> service.approveBooking(BOOKING_ID, OWNER_ID, false));

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepo).save(captor.capture());
        Booking captBook = captor.getValue();
        assertNotNull(captBook);
        assertEquals(REJECTED, captBook.getStatus());
    }

    private Booking getBooking() {
        User owner = new User();
        owner.setId(OWNER_ID);

        User booker = new User();
        booker.setId(BOOKER_ID);

        Item item = new Item();
        item.setOwner(owner);
        item.setId(ITEM_ID);
        item.setAvailable(true);

        Booking book = new Booking();
        book.setId(BOOKING_ID);
        book.setItem(item);
        book.setStatus(WAITING);
        book.setBooker(booker);

        return book;
    }
}