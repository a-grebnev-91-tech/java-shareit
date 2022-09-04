package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.controller.dto.CommentRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.domain.Comment;
import ru.practicum.shareit.user.mapper.UserReferenceMapper;

@Mapper(componentModel = "spring", uses = {UserReferenceMapper.class, ItemReferenceMapper.class})
public interface CommentMapper {
    @Mapping(source = "itemId", target = "item")
    @Mapping(source = "authorId", target = "author")
    Comment toModel(CommentRequest request, Long itemId, Long authorId);

    @Mapping(source = "author.name", target = "authorName")
    CommentResponse toResponse(Comment comment);
}
