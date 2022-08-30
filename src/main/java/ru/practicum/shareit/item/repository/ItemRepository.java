package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    List<Item> findAllByOwnerId(long ownerId);

    Optional<Item> findById(long itemId);

    List<Item> findByNameAndDescription(String text);
}
