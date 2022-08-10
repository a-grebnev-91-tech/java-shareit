package ru.practicum.shareit.requests.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.entity.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestModel;

@Mapper
public interface ItemRequestMapper {
    ItemRequestModel dtoToModel(ItemRequestDto dto);

    ItemRequestModel entityToModel(ItemRequest entity);

    ItemRequestDto modelToDto(ItemRequestModel model);

    ItemRequest modelToEntity(ItemRequestModel model);
}
