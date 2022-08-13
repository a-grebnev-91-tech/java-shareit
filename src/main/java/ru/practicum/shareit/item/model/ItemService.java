package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.controller.ItemPatchRequest;
import ru.practicum.shareit.item.controller.ItemRequest;
import ru.practicum.shareit.item.controller.ItemResponse;

import java.util.List;

public interface ItemService {
    ItemResponse createItem(long userId, ItemRequest dto);

    List<ItemResponse> getAllByUser(long userId);

    ItemResponse getItem(long id);

    ItemResponse patchItem(long userId, long itemId, ItemPatchRequest patchRequest);

    List<ItemResponse> searchItem(String text);
}
