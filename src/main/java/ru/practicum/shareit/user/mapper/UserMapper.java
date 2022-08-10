package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.model.UserModel;

@Mapper
public interface UserMapper {
    UserModel dtoToModel(UserDto dto);

    UserModel entityToModel(User entity);

    UserDto modelToDto(UserModel model);

    User modelToEntity(UserModel model);
}
