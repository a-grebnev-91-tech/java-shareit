package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.ItemRequest;
import ru.practicum.shareit.item.controller.ItemResponse;
import ru.practicum.shareit.item.dao.Item;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper mapper;

    @Override
    public ItemResponse createItem(long userId, ItemRequest dto) {
        dto.setOwnerId(userId);
        ItemModel model = mapper.requestToModel(dto);
        Item entity = itemRepository.createItem(mapper.modelToEntity(model));
        model = mapper.entityToModel(entity);
        return mapper.modelToResponse(model);
    }

    private void throwExceptionIfUserNotExist(long userId) {
        if (userRepository.getUser(userId).isEmpty()) {
            throw new NotFoundException(String.format("User with id %d isn't exist", userId));
        }
    }}
