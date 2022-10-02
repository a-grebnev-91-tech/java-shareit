package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingOutputDto {
    private Long id;
    private BookerDto booker;
    private BookedItemDto item;
    private String status;
    private LocalDateTime start;
    private LocalDateTime end;
}
