package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.controller.dto.LastBooking;
import ru.practicum.shareit.booking.controller.dto.NextBooking;

@Getter
@Setter
public class ItemOwnerResponse extends ItemResponse {
    private LastBooking lastBooking;
    private NextBooking nextBooking;
}
