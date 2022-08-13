package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.controller.ItemRequest;
import ru.practicum.shareit.item.controller.ItemResponse;
import ru.practicum.shareit.item.dao.Item;
import ru.practicum.shareit.item.model.ItemModel;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

@Mapper(componentModel = "spring", uses = {UserReferenceMapper.class})
public interface ItemMapper {
    @Mapping(source = "ownerId", target = "owner")
    ItemModel requestToModel(ItemRequest dto);

    @Mapping(source = "ownerId", target = "owner")
    ItemModel entityToModel(Item entity);

    ItemResponse modelToResponse(ItemModel model);

    @Mapping(source = "owner", target = "ownerId")
    Item modelToEntity(ItemModel model);
}