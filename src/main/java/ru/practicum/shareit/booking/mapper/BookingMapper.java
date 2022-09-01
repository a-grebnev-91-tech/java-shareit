package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemReferenceMapper;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserReferenceMapper.class, ItemReferenceMapper.class})
public interface BookingMapper {
    @Mapping(source = "dto.itemId", target = "item")
    @Mapping(source = "bookerId", target = "booker")
    Booking toModel(BookingRequest dto,Long bookerId);

    BookingResponse toResponse(Booking model);

    List<BookingResponse> toResponse(List<Booking> modelList);
}
