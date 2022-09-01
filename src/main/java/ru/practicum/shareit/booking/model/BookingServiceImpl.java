package ru.practicum.shareit.booking.model;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingStateIsNotSupportedException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository bookingRepository;
    @Qualifier("InDbUsers")
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponse approveBooking(Long bookingId, Long userId, boolean approved) {
        Booking booking = getBookingOrThrow(bookingId);
        if (isOwner(userId, booking) && booking.getStatus() == BookingStatus.WAITING) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            return mapper.toResponse(bookingRepository.save(booking));
        } else {
            throw new NotAvailableException("Could not change booking status");
        }
    }

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest, Long userId) {
        Booking booking = mapper.toModel(bookingRequest, userId);
        Item requestedItem = booking.getItem();
        if (requestedItem.isAvailable()) {
            return mapper.toResponse(bookingRepository.save(booking));
        } else {
            throw new NotAvailableException(String.format("Item with id %d isn't available", requestedItem.getId()));
        }
    }

    @Override
    public List<BookingResponse> getAllBookingsByBooker(Long bookerId, String state) {
        BookingsState bookingState = convertToBookingState(state);
        checkUserExisting(bookerId);
        List<Booking> bookings;
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        if (bookingState.equals(BookingsState.WAITING) || bookingState.equals(BookingsState.REJECTED)) {
            bookings =
                    bookingRepository
                    .findAllByBookerIdAndStatus(bookerId, BookingStatus.valueOf(state), sort);
        } else {
            bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
            if (!bookingState.equals(BookingsState.ALL)) {
                bookings = filterBookingByState(bookings, bookingState);
            }
        }
        return mapper.toResponse(bookings);
    }

    @Override
    public BookingResponse getBooking(Long bookingId, Long userId) {
        Booking booking = getBookingOrThrow(bookingId);
        if (isBooker(userId, booking) || isOwner(userId, booking)) {
            return mapper.toResponse(booking);
        } else {
            throw new NotFoundException(
                    String.format("User with id %d doesn't have access to booking with id %d", userId, bookingId)
            );
        }
    }

    private void checkUserExisting(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %d isn't exist", userId));
        }
    }

    private BookingsState convertToBookingState(String state) {
        try {
            return BookingsState.valueOf(state);
        } catch (IllegalArgumentException ex) {
            throw new BookingStateIsNotSupportedException(String.format("Unknown state: %s", state));
        }
    }

    private Booking getBookingOrThrow(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.orElseThrow(
                () -> new NotFoundException(String.format("Booking with id %d isn't exist", bookingId))
        );
    }

    //todo del
    private List<Booking> filterBookingByState(List<Booking> bookings, BookingsState state) {
        switch (state) {
            case CURRENT:
                return bookings.stream()
                        .filter(this::bookingIsCurrent)
                        .collect(Collectors.toList());
            case PAST:
                return bookings.stream()
                        .filter(this::bookingInPast)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream()
                        .filter(this::bookingInFuture)
                        .collect(Collectors.toList());
            default:
                throw new BookingStateIsNotSupportedException(String.format("Booking state %s isn't supported", state.name()));
        }
    }

    private boolean bookingInFuture(Booking booking) {
        return booking.getStart().isAfter(LocalDateTime.now());
    }

    private boolean bookingInPast(Booking booking) {
        return booking.getEnd().isBefore(LocalDateTime.now());
    }

    private boolean bookingIsCurrent(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        return booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
    }

    private boolean isBooker(Long userId, Booking booking) {
        if (userId == null)
            return false;
        return userId.equals(booking.getBooker().getId());
    }

    private boolean isOwner(Long userId, Booking booking) {
        if (userId == null)
            return false;
        return userId.equals(booking.getItem().getOwner().getId());
    }
}
