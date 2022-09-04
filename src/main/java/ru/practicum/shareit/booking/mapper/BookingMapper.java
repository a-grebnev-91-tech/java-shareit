package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.controller.dto.*;
import ru.practicum.shareit.booking.domain.Booking;
import ru.practicum.shareit.item.mapper.ItemReferenceMapper;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserReferenceMapper.class, ItemReferenceMapper.class})
public interface BookingMapper {
    @Mapping(source = "booking.booker.id", target = "bookerId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBookingForItemFromBooking(Booking booking, @MappingTarget BookingForItem bookingForItem);

    @Mapping(source = "dto.itemId", target = "item")
    @Mapping(source = "bookerId", target = "booker")
    Booking toModel(BookingRequest dto,Long bookerId);

    BookingResponse toResponse(Booking model);

    List<BookingResponse> toResponse(List<Booking> modelList);
}
