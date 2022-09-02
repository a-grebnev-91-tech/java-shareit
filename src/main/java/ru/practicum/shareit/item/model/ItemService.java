package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.controller.dto.CommentRequest;
import ru.practicum.shareit.item.controller.dto.CommentResponse;
import ru.practicum.shareit.item.controller.dto.ItemRequest;
import ru.practicum.shareit.item.controller.dto.ItemResponse;

import java.util.List;

public interface ItemService {
    CommentResponse createComment(CommentRequest commentDto, Long itemId, Long userId);

    ItemResponse createItem(long userId, ItemRequest dto);

    List<ItemResponse> getAllByUser(long userId);

    ItemResponse getItem(long itemId, long userId);

    ItemResponse patchItem(long userId, long itemId, ItemRequest patchRequest);

    List<ItemResponse> searchItem(String text);
}
