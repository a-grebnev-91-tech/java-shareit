package ru.practicum.shareit.item.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.controller.dto.BookingForItemDto;
import ru.practicum.shareit.booking.controller.dto.ClosestBookings;

@Getter
@Setter
public class ItemForOwnerOutputDto extends ItemOutputDto {
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private ClosestBookings closestBookings;

    public void setClosestBookings(ClosestBookings closestBookings) {
        this.closestBookings = closestBookings;
        this.lastBooking = closestBookings.getLastBooking();
        this.nextBooking = closestBookings.getNextBooking();
    }
}
