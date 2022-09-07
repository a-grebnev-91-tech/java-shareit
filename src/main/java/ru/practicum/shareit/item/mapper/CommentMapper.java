package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.controller.dto.CommentInputDto;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.domain.Comment;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

@Mapper(componentModel = "spring", uses = {UserReferenceMapper.class, ItemReferenceMapper.class})
public interface CommentMapper {
    @Mapping(source = "itemId", target = "item")
    @Mapping(source = "authorId", target = "author")
    Comment toModel(CommentInputDto request, Long itemId, Long authorId);

    @Mapping(source = "author.name", target = "authorName")
    CommentOutputDto toResponse(Comment comment);
}
