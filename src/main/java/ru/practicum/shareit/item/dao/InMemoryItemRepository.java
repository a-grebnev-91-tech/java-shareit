package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

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

    private long generateId() {
        return currentId++;
    }
}
