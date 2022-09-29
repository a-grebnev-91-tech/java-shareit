package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.domain.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentReferenceMapper {
    private final CommentRepository repository;
    private final CommentMapper mapper;

    List<CommentOutputDto> map(Long itemId) {
        List<Comment> comments = repository.findAllByItemId(itemId);
        return comments.stream().map(mapper::toResponse).collect(Collectors.toList());
    }
}
