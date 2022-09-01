package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;

public interface BookingService {
    BookingResponse approveBooking(Long bookingId, Long userId, boolean approved);

    BookingResponse getBooking(Long bookingId, Long userId);

    BookingResponse createBooking(BookingRequest request, Long userId);
}
