package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.controller.ItemRequest;
import ru.practicum.shareit.item.controller.ItemResponse;

import java.util.Optional;

public interface ItemService {
    ItemResponse createItem(long userId, ItemRequest dto);
}
