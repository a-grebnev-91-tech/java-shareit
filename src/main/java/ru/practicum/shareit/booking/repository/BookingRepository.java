package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository {
    Booking save(Booking booking);
}
