package ru.practicum.shareit.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.requests.controller.dto.RequestInputDto;
import ru.practicum.shareit.requests.controller.dto.RequestOutputDto;
import ru.practicum.shareit.requests.domain.Request;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;


@Mapper(componentModel = "spring", uses = {UserReferenceMapper.class, ItemMapper.class})
public interface RequestMapper {
    @Mapping(source = "requesterId", target = "requester")
    Request dtoToModel(RequestInputDto dto, Long requesterId);

    @Mapping(source = "responses", target = "items", qualifiedByName = "itemToDto")
    RequestOutputDto modelToDto(Request model);
}
