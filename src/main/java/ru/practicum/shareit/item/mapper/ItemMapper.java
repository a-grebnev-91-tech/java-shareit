package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.booking.mapper.BookingReferenceMapper;
import ru.practicum.shareit.item.controller.dto.ItemForOwnerOutputDto;
import ru.practicum.shareit.item.controller.dto.ItemInputDto;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.requests.mapper.RequestReferenceMapper;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

@Mapper(
        componentModel = "spring",
        uses = {UserReferenceMapper.class,
                BookingReferenceMapper.class,
                CommentReferenceMapper.class,
                RequestReferenceMapper.class
        }
)
public interface ItemMapper {

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "dto.requestId", target = "request")
    Item toModel(ItemInputDto dto, Long ownerId);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "id", target = "lastBooking")
    @Mapping(source = "id", target = "nextBooking")
    @Mapping(source = "id", target = "comments")
    ItemForOwnerOutputDto toOwnerResponse(Item model);

    @Named("itemToDto")
    @Mapping(source = "id", target = "comments")
    @Mapping(source = "request", target = "requestId")
    ItemOutputDto toResponse(Item model);
}