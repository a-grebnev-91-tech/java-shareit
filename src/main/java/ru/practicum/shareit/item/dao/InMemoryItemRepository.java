package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private long currentId;
    private final Map<Long, Item> items;

    public InMemoryItemRepository() {
        currentId = 1;
        items = new HashMap<>();
    }

    @Override
    public Item createItem(Item item) {
        long id = generateId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public List<Item> getAllByUser(long userId) {
        return items.values().stream().filter(item -> item.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getItem(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> searchItem(String text) {
        return items.values()
                .stream()
                .filter(item -> item.isAvailable() && isItemSuitForText(item, text))
                .collect(Collectors.toList());
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    private long generateId() {
        return currentId++;
    }

    private boolean isItemSuitForText(Item item, String text) {
        if (text.isBlank())
            return false;
        text = text.toLowerCase();
        return item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text);
    }
}
