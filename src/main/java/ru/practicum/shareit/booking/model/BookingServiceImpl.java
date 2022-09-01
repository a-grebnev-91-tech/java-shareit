package ru.practicum.shareit.booking.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest, Long userId) {
        Booking booking = mapper.toModel(bookingRequest, userId);
        Item requestedItem = booking.getItem();
        if (requestedItem.isAvailable()) {
            return mapper.toResponse(bookingRepository.save(booking));
        } else {
            throw new ItemNotAvailableException(String.format("Item with id %d isn't available", requestedItem.getId()));
        }
    }

    @Override
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
            throw new ItemNotAvailableException("Could not change booking status");
        }
    }

    private boolean isOwner(Long userId, Booking booking) {
        return userId.equals(booking.getItem().getOwner().getId());
    }

    private Booking getBookingOrThrow(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return booking.orElseThrow(
                () -> new NotFoundException(String.format("Booking with id %d isn't exist", bookingId))
        );
    }
}
