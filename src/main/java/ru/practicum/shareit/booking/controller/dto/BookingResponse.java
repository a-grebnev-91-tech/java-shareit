package ru.practicum.shareit.booking.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingResponse {
    private Long id;
    private String status;
    private Booker booker;
    private BookedItem item;
}
