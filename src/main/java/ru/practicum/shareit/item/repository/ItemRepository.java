package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item);

    List<Item> getAllByUser(long userId);

    Optional<Item> getItem(long itemId);

    List<Item> searchItem(String text);

    Item updateItem(Item item);
}
