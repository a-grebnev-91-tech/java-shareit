package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.domain.Item;

import java.util.*;
import java.util.stream.Collectors;

//TODO remove all in memory shit
@Repository("InMemoryItems")
public class InMemoryItemRepository implements ItemRepository {
    private long currentId;
    private final Map<Long, Item> items;

    public InMemoryItemRepository() {
        currentId = 1;
        items = new HashMap<>();
    }

    @Override
    public Item save(Item item) {
        if (items.containsKey(item.getId())) {
            return update(item);
        } else {
            return createItem(item);
        }
    }

    @Override
    public List<Item> findAllByOwnerId(long ownerId, Pageable pageable) {
        return items.values().stream().filter(item -> item.getOwner().getId() == ownerId).collect(Collectors.toList());
    }

    @Override
    public Optional<Item> findById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> findByNameAndDescription(String text, Pageable pageable) {
        return items.values()
                .stream()
                .filter(item -> item.isAvailable() && isItemSuitForText(item, text))
                .collect(Collectors.toList());
    }

    private Item createItem(Item item) {
        long id = generateId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    private long generateId() {
        return currentId++;
    }

    private boolean isItemSuitForText(Item item, String text) {
        text = text.toLowerCase();
        return item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text);
    }

    private Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }
}
