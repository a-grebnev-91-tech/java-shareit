package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.domain.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class ItemReferenceMapper {
    @Qualifier("InDbItems")
    private final ItemRepository repo;

    public Item map(@NotNull Long itemId) {
        return repo.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format("Item with id %d isn't exist", itemId))
        );
    }

    public Long map(@NotNull Item item) {
        return item.getId();
    }
}
