package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.controller.ItemRequest;
import ru.practicum.shareit.item.controller.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

@Mapper(componentModel = "spring", uses = {UserReferenceMapper.class})
public interface ItemMapper {

    ItemResponse toResponse(Item model);

    @Mapping(source = "ownerId", target = "owner")
    Item toModel(ItemRequest dto);
}