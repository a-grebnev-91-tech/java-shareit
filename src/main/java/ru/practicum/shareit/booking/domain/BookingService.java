package ru.practicum.shareit.booking.domain;

import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse approveBooking(Long bookingId, Long userId, boolean approved);

    BookingResponse createBooking(BookingRequest request, Long userId);

    List<BookingResponse> getAllBookingsByBooker(Long userId, String state, String sortBy, String order);

    List<BookingResponse> getAllBookingsByOwner(Long userId, String state);

    BookingResponse getBooking(Long bookingId, Long userId);
}
