package ru.practicum.shareit.item.dao;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item);

    List<Item> getAllByUser(long userId);

    Optional<Item> getItem(long itemId);

    List<Item> searchItem(String text);

    Item updateItem(Item item);
}
