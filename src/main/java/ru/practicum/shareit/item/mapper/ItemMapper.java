package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.model.ItemModel;

@Mapper
public interface ItemMapper {
    ItemModel dtoToModel(ItemDto dto);

    ItemModel entityToModel(Item entity);

    ItemDto modelToDto(ItemModel model);

    Item modelToEntity(ItemModel model);
}
