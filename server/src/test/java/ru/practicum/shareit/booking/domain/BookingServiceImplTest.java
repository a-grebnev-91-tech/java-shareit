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
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingParamObj;
import ru.practicum.shareit.booking.controller.dto.BookingInputDto;
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

    private Booking book;
    private BookingInputDto bookInputDto;

    @Mock
    private UserRepository userRepo;
    @Mock
    private BookingRepository bookingRepo;
    @Mock
    private BookingMapper mapper;
    @InjectMocks
    private BookingServiceImpl service;

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

        bookInputDto = new BookingInputDto();
        bookInputDto.setItemId(ITEM_ID);
        when(mapper.dtoToModel(bookInputDto, BOOKER_ID)).thenReturn(book);
        when(mapper.dtoToModel(bookInputDto, OWNER_ID)).thenAnswer(
                invocationOnMock -> {
                    book.setBooker(book.getItem().getOwner());
                    return book;
                }
        );
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

    @Test
    void test10_notExistingUserAttemptToGetBooking() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.getBooking(BOOKING_ID, 999L)
        );
        assertNotNull(throwable.getMessage());
    }

    //TODO исправить после завершения проекта на NotAvailable
    @Test
    void test11_outsideUserAttemptToGetBooking() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.getBooking(BOOKING_ID, OUTSIDE_USER_ID)
        );
        assertNotNull(throwable.getMessage());
    }

    //TODO исправить после завершения проекта на NotAvailable
    @Test
    void test12_nullUserAttemptToGetBooking() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.getBooking(BOOKING_ID, null)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test13_nonExistingUserAttemptToGetNonExistingBooking() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.getBooking(999L, 999L)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test14_outsideUserAttemptToGetNonExistingBooking() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.getBooking(999L, OUTSIDE_USER_ID)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test15_nonExistingUserAttemptToGetNullBooking() {
        Throwable throwable = assertThrows(
                NotAvailableException.class,
                () -> service.getBooking(null, 999L)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test16_outsideUserAttemptToGetNullBooking() {
        Throwable throwable = assertThrows(
                NotAvailableException.class,
                () -> service.getBooking(null, OUTSIDE_USER_ID)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test17_ownerOrBookerGetBooking() {
        assertDoesNotThrow(() -> service.getBooking(BOOKING_ID, BOOKER_ID));
        assertDoesNotThrow(() -> service.getBooking(BOOKING_ID, OWNER_ID));
        verify(bookingRepo, times(2)).findById(BOOKING_ID);
    }

    @Test
    void test18_bookerBookedAvailableItem() {
        assertDoesNotThrow(() -> service.createBooking(bookInputDto, BOOKER_ID));

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepo).save(captor.capture());

        Booking capturedBook = captor.getValue();
        assertNotNull(capturedBook);
        assertEquals(BOOKING_ID, capturedBook.getId());
        assertEquals(WAITING, capturedBook.getStatus());

        User capturedBooker = capturedBook.getBooker();
        assertNotNull(capturedBooker);
        assertEquals(BOOKER_ID, capturedBooker.getId());

        Item capturedItem = capturedBook.getItem();
        assertNotNull(capturedItem);
        assertEquals(ITEM_ID, capturedItem.getId());

        User capturedOwner = capturedItem.getOwner();
        assertNotNull(capturedOwner);
        assertEquals(OWNER_ID, capturedOwner.getId());
    }

    @Test
    void test19_ownerAttemptToBookedAvailableItem() {
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.createBooking(bookInputDto, OWNER_ID)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test20_bookerAttemptToBookedUnavailableItem() {
        book.getItem().setAvailable(false);
        Throwable throwable = assertThrows(
                NotAvailableException.class,
                () -> service.createBooking(bookInputDto, BOOKER_ID)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test21_notExistingBookerAttemptToGetAllHisBookingsByState() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(999L).withState("ALL").build();
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.getAllBookingsByBooker(paramObj)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test22_nullBookerAttemptToGetAllHisBookingsByState() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(null).withState("ALL").build();
        Throwable throwable = assertThrows(
                NullPointerException.class,
                () -> service.getAllBookingsByBooker(paramObj)
        );
    }

    @Test
    void test23_bookerAttemptToGetAllHisBookingsByStateWaiting() {
        BookingStatus waiting = WAITING;
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(BOOKER_ID).withState(waiting.name()).build();
        assertDoesNotThrow(() -> service.getAllBookingsByBooker(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BookingStatus> statusCaptor = ArgumentCaptor.forClass(BookingStatus.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllByBookerIdAndStatus(longCaptor.capture(), statusCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        BookingStatus capturedStatus = statusCaptor.getValue();
        assertNotNull(capturedStatus);
        assertEquals(waiting, capturedStatus);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test24_bookerAttemptToGetAllHisBookingsByStateRejected() {
        BookingStatus rejected = REJECTED;
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(BOOKER_ID).withState(rejected.name()).build();
        assertDoesNotThrow(() -> service.getAllBookingsByBooker(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BookingStatus> statusCaptor = ArgumentCaptor.forClass(BookingStatus.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllByBookerIdAndStatus(longCaptor.capture(), statusCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        BookingStatus capturedStatus = statusCaptor.getValue();
        assertNotNull(capturedStatus);
        assertEquals(rejected, capturedStatus);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test25_bookerAttemptToGetAllHisBookingsByStateAll() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(BOOKER_ID).withState("ALL").build();
        assertDoesNotThrow(() -> service.getAllBookingsByBooker(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllByBookerId(longCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test26_bookerAttemptToGetAllHisBookingsByStatePast() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(BOOKER_ID).withState("PAST").build();
        assertDoesNotThrow(() -> service.getAllBookingsByBooker(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllPastByBooker(longCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test27_bookerAttemptToGetAllHisBookingsByStateCurrent() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(BOOKER_ID).withState("CURRENT").build();
        assertDoesNotThrow(() -> service.getAllBookingsByBooker(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllCurrentByBooker(longCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test28_bookerAttemptToGetAllHisBookingsByStateFuture() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(BOOKER_ID).withState("FUTURE").build();
        assertDoesNotThrow(() -> service.getAllBookingsByBooker(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllComingByBooker(longCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test29_notExistingOwnerAttemptToGetAllHisBookingsByState() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(999L).withState("ALL").build();
        Throwable throwable = assertThrows(
                NotFoundException.class,
                () -> service.getAllBookingsByOwner(paramObj)
        );
        assertNotNull(throwable.getMessage());
    }

    @Test
    void test30_nullOwnerAttemptToGetAllHisBookingsByState() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(null).withState("ALL").build();
        Throwable throwable = assertThrows(
                NullPointerException.class,
                () -> service.getAllBookingsByOwner(paramObj)
        );
    }

    @Test
    void test31_ownerAttemptToGetAllHisBookingsByStateWaiting() {
        BookingStatus waiting = WAITING;
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(OWNER_ID).withState(waiting.name()).build();
        assertDoesNotThrow(() -> service.getAllBookingsByOwner(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BookingStatus> statusCaptor = ArgumentCaptor.forClass(BookingStatus.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllByOwnerIdAndStatus(longCaptor.capture(), statusCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        BookingStatus capturedStatus = statusCaptor.getValue();
        assertNotNull(capturedStatus);
        assertEquals(waiting, capturedStatus);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test32_ownerAttemptToGetAllHisBookingsByStateRejected() {
        BookingStatus rejected = REJECTED;
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(OWNER_ID).withState(rejected.name()).build();
        assertDoesNotThrow(() -> service.getAllBookingsByOwner(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BookingStatus> statusCaptor = ArgumentCaptor.forClass(BookingStatus.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllByOwnerIdAndStatus(longCaptor.capture(), statusCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        BookingStatus capturedStatus = statusCaptor.getValue();
        assertNotNull(capturedStatus);
        assertEquals(rejected, capturedStatus);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test33_ownerAttemptToGetAllHisBookingsByStateAll() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(OWNER_ID).withState("ALL").build();
        assertDoesNotThrow(() -> service.getAllBookingsByOwner(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllByOwnerId(longCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test34_ownerAttemptToGetAllHisBookingsByStatePast() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(OWNER_ID).withState("PAST").build();
        assertDoesNotThrow(() -> service.getAllBookingsByOwner(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllPastByOwnerId(longCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test35_ownerAttemptToGetAllHisBookingsByStateCurrent() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(OWNER_ID).withState("CURRENT").build();
        assertDoesNotThrow(() -> service.getAllBookingsByOwner(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllCurrentByOwnerId(longCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void test36_ownerAttemptToGetAllHisBookingsByStateFuture() {
        BookingParamObj paramObj = BookingParamObj.newBuilder().withUserId(OWNER_ID).withState("FUTURE").build();
        assertDoesNotThrow(() -> service.getAllBookingsByOwner(paramObj));

        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(bookingRepo)
                .findAllComingByOwnerId(longCaptor.capture(), pageableCaptor.capture());

        Long capturedId = longCaptor.getValue();
        assertNotNull(capturedId);
        assertEquals(paramObj.getUserId(), capturedId);

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable);
        assertEquals(paramObj.getPageable(), capturedPageable);

        verifyNoMoreInteractions(bookingRepo);
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