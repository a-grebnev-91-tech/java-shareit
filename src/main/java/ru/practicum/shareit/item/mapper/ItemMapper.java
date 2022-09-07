package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.mapper.BookingReferenceMapper;
import ru.practicum.shareit.item.controller.dto.ItemForOwnerOutputDto;
import ru.practicum.shareit.item.controller.dto.ItemInputDto;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

@Mapper(
        componentModel = "spring",
        uses = {UserReferenceMapper.class, BookingReferenceMapper.class, CommentReferenceMapper.class}
)
public interface ItemMapper {

    @Mapping(source = "ownerId", target = "owner")
    Item toModel(ItemInputDto dto, Long ownerId);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "id", target = "lastBooking")
    @Mapping(source = "id", target = "nextBooking")
    @Mapping(source = "id", target = "comments")
    ItemForOwnerOutputDto toOwnerResponse(Item model);

    @Mapping(source = "id", target = "comments")
    ItemOutputDto toResponse(Item model);
}