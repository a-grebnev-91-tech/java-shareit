package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.controller.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", uses = {UserReferenceMapper.class})
public interface UserMapper {
    UserDto toDto(User model);

    User toModel(UserDto dto);

    User toModel(Long id);
}
