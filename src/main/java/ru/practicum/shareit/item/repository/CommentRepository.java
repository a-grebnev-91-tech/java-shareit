package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.domain.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findAllByItemId(Long itemId);
}
