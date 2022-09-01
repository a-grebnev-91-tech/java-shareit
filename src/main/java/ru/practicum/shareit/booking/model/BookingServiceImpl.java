package ru.practicum.shareit.booking.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository bookingRepository;

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

    private Booking getBookingOrThrow(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.orElseThrow(
                () -> new NotFoundException(String.format("Booking with id %d isn't exist", bookingId))
        );
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
