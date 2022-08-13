package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.ItemPatchRequest;
import ru.practicum.shareit.item.controller.ItemRequest;
import ru.practicum.shareit.item.controller.ItemResponse;
import ru.practicum.shareit.item.dao.Item;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;

    @Override
    public ItemResponse createItem(long userId, ItemRequest dto) {
        dto.setOwnerId(userId);
        ItemModel model = mapper.requestToModel(dto);
        Item entity = itemRepository.createItem(mapper.modelToEntity(model));
        return mapper.entityToResponse(entity);
    }

    @Override
    public List<ItemResponse> getAllByUser(long userId) {
        getUserOrThrow(userId);
        return itemRepository.getAllByUser(userId).stream().map(mapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    public ItemResponse getItem(long id) {
        return mapper.entityToResponse(itemRepository.getItem(id).orElseThrow(
                () -> new NotFoundException(String.format("Item with id %d isn't exist", id)
        )));
    }

    @Override
    public ItemResponse patchItem(long userId, long itemId, ItemPatchRequest itemPatch) {
        getUserOrThrow(userId);
        Item existingItem = getItemOrThrow(itemId);
        if (existingItem.getOwnerId() != userId) {
            throw new ForbiddenOperationException(
                    String.format("User with id %d could not change item with id %d", userId, existingItem.getId())
            );
        }
        Arrays.stream(itemPatch.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> {
                    try {
                        return field.get(itemPatch) != null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList())
                .forEach(fieldToPatch -> {
                    try {
                        Field existingUserField = existingItem.getClass().getDeclaredField(fieldToPatch.getName());
                        existingUserField.setAccessible(true);
                        existingUserField.set(existingItem, fieldToPatch.get(itemPatch));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        return mapper.entityToResponse(itemRepository.updateItem(existingItem));
    }

    @Override
    public List<ItemResponse> searchItem(String text) {
        List<Item> founded = itemRepository.searchItem(text);
        return founded.stream().map(mapper::entityToResponse).collect(Collectors.toList());
    }

    private Item getItemOrThrow(long itemId) {
        Optional<Item> item = itemRepository.getItem(itemId);
        return item.orElseThrow(() -> new NotFoundException(String.format("Item with id %d isn't exist", itemId)));

    }

    private User getUserOrThrow(long userId) {
        Optional<User> user = userRepository.getUser(userId);
        return user.orElseThrow(() -> new NotFoundException(String.format("User with id %d isn't exist", userId)));
    }
}
