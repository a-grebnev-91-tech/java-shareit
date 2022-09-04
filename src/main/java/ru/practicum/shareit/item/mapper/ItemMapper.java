package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.mapper.BookingReferenceMapper;
import ru.practicum.shareit.item.controller.dto.ItemOwnerResponse;
import ru.practicum.shareit.item.controller.dto.ItemRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

@Mapper(
        componentModel = "spring",
        uses = {UserReferenceMapper.class, BookingReferenceMapper.class, CommentReferenceMapper.class}
)
public interface ItemMapper {

    @Mapping(source = "ownerId", target = "owner")
    Item toModel(ItemRequest dto, Long ownerId);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "id", target = "lastBooking")
    @Mapping(source = "id", target = "nextBooking")
    @Mapping(source = "id", target = "comments")
    ItemOwnerResponse toOwnerResponse(Item model);

    @Mapping(source = "id", target = "comments")
    ItemResponse toResponse(Item model);
}