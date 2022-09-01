package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, Long userId);

    BookingResponse approveBooking(Long bookingId, Long userId, boolean approved);
}
