package ru.practicum.shareit.booking.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingResponse {
    private Long id;
    private Booker booker;
    private BookedItem item;
    private String status;
    private LocalDateTime start;
    private LocalDateTime end;
}
