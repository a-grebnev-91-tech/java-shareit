package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.model.BookingModel;

@Mapper
public interface BookingMapper {
    BookingModel dtoToModel(BookingDto dto);

    BookingModel entityToModel(Booking entity);

    BookingDto modelToDto(BookingModel model);

    Booking modelToEntity(BookingModel model);
}
