package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.controller.dto.LastBookingDto;
import ru.practicum.shareit.booking.controller.dto.NextBookingDto;

@Getter
@Setter
public class ItemForOwnerOutputDto extends ItemOutputDto {
    private LastBookingDto lastBooking;
    private NextBookingDto nextBooking;
}
