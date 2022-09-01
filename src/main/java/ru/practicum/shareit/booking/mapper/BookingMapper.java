package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.booking.controller.dto.Booker;
import ru.practicum.shareit.booking.controller.dto.BookingRequest;
import ru.practicum.shareit.booking.controller.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemReferenceMapper;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;
import ru.practicum.shareit.user.model.User;

import java.awt.print.Book;
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
