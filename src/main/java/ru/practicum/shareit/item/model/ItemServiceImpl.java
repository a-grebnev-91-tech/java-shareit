package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PatchException;
import ru.practicum.shareit.item.controller.ItemRequest;
import ru.practicum.shareit.item.controller.ItemResponse;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.Patcher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Qualifier("InDbItems")
    private final ItemRepository itemRepository;
    @Qualifier("InDbUsers")
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final Patcher patcher;

    @Override
    public ItemResponse createItem(long userId, ItemRequest dto) {
        return itemMapper.toResponse(itemRepository.save(itemMapper.toModel(dto, userId)));
    }

    @Override
    public List<ItemResponse> getAllByUser(long userId) {
        getUserOrThrow(userId);
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(itemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponse getItem(long id) {
        return itemMapper.toResponse(itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Item with id %d isn't exist", id))
        ));
    }

    @Override
    @Transactional
    public ItemResponse patchItem(long userId, long itemId, ItemRequest patch) {
        Item existingItem = getItemOrThrow(itemId);
        if (isItemBelongToUser(existingItem, userId)) {
            if (patcher.patch(existingItem, patch)) {
                return itemMapper.toResponse(itemRepository.save(existingItem));
            } else {
                throw new PatchException(String.format("Patch %s couldn't be applied on %s", patch, existingItem));
            }
        } else {
            throw new ForbiddenOperationException(
                    String.format("User with id %d could not change item with id %d", userId, existingItem.getId())
            );
        }
    }

    @Override
    public List<ItemResponse> searchItem(String text) {
        if (text.isBlank())
            return Collections.emptyList();
        List<Item> founded = itemRepository.findByNameAndDescription(text);
        return founded.stream().map(itemMapper::toResponse).collect(Collectors.toList());
    }

    private Item getItemOrThrow(long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.orElseThrow(() -> new NotFoundException(String.format("Item with id %d isn't exist", itemId)));

    }

    private User getUserOrThrow(long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new NotFoundException(String.format("User with id %d isn't exist", userId)));
    }

    private boolean isItemBelongToUser(Item existingItem, long userId) {
        getUserOrThrow(userId);
        return existingItem.getOwner().getId().equals(userId);
    }
}
