package ru.practicum.shareit.item.domain;

import ru.practicum.shareit.item.ItemsParamObject;
import ru.practicum.shareit.item.controller.dto.CommentInputDto;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.controller.dto.ItemInputDto;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;

import java.util.List;

public interface ItemService {
    CommentOutputDto createComment(CommentInputDto commentDto, Long itemId, Long userId);

    ItemOutputDto createItem(long userId, ItemInputDto dto);

    List<ItemOutputDto> getAllByUser(ItemsParamObject params);

    ItemOutputDto getItem(long itemId, long userId);

    ItemOutputDto patchItem(long userId, long itemId, ItemInputDto patchRequest);

    List<ItemOutputDto> searchItem(ItemsParamObject params);
}
