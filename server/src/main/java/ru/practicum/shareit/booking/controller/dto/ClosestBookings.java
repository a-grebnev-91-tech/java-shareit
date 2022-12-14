package ru.practicum.shareit.booking.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ClosestBookings {
    private final BookingForItemDto lastBooking;
    private final BookingForItemDto nextBooking;
}
