package ru.practicum.shareit.booking.domain;

import ru.practicum.shareit.booking.BookingParamObj;
import ru.practicum.shareit.booking.controller.dto.BookingOutputDto;
import ru.practicum.shareit.booking.controller.dto.BookingInputDto;

import java.util.List;

public interface BookingService {
    BookingOutputDto approveBooking(Long bookingId, Long userId, boolean approved);

    BookingOutputDto createBooking(BookingInputDto request, Long userId);

    List<BookingOutputDto> getAllBookingsByBooker(BookingParamObj paramObj);

    List<BookingOutputDto> getAllBookingsByOwner(BookingParamObj paramObj);

    BookingOutputDto getBooking(Long bookingId, Long userId);
}
