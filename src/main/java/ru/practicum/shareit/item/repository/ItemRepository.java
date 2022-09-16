package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.domain.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);

    List<Item> findAllByOwnerId(long ownerId, Pageable pageable);

    Optional<Item> findById(long itemId);

    List<Item> findByNameAndDescription(String text, Pageable pageable);
}
