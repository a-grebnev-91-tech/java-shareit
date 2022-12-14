package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemsParamObject;
import ru.practicum.shareit.item.controller.dto.CommentInputDto;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.controller.dto.ItemInputDto;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;
import ru.practicum.shareit.item.domain.ItemService;

import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping("{itemId}/comment")
    public CommentOutputDto createComment(
            @PathVariable("itemId") Long itemId,
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody CommentInputDto comment
    ) {
        log.info("User with id {} attempt to post comment to item {}",userId, itemId);
        return service.createComment(comment, itemId, userId);
    }

    @PostMapping
    public ItemOutputDto createItem(
            @RequestHeader(value = USER_ID_HEADER) long userId,
            @RequestBody ItemInputDto dto
    ) {
        log.info("Creating user {}", dto);
        return service.createItem(userId, dto);
    }

    @GetMapping
    public List<ItemOutputDto> getAll(
            @RequestHeader(value = USER_ID_HEADER) long userId,
            @RequestParam(name = "from") int from,
            @RequestParam(name = "size") int size,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "order") String order
    ) {
        log.info("Obtaining all items");
        ItemsParamObject params = ItemsParamObject.newBuilder().withUserId(userId).from(from).size(size).sortBy(sortBy)
                .sortOrder(order).build();
        return service.getAllByUser(params);
    }

    @GetMapping("/{id}")
    public ItemOutputDto getItem(@PathVariable("id") long itemId, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("User with id {} obtaining item with id {}", userId, itemId);
        return service.getItem(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemOutputDto patchItem(
            @RequestHeader(value = USER_ID_HEADER) long userId,
            @PathVariable("itemId") long itemId,
            @RequestBody ItemInputDto patchRequest
    ) {
        log.info("User with id {} updating {}", userId, patchRequest);
        return service.patchItem(userId, itemId, patchRequest);
    }

    @GetMapping("/search")
    public List<ItemOutputDto> searchItems(
            @RequestParam String text,
            @RequestHeader(value = USER_ID_HEADER) long userId,
            @RequestParam(name = "from") Integer from,
            @RequestParam(name = "size") Integer size,
            @RequestParam(value = "sortBy") String sortBy,
            @RequestParam(value = "order") String order
    ) {
        log.info("Searching item by {}", text);
        ItemsParamObject params = ItemsParamObject.newBuilder().withUserId(userId).withText(text).from(from).size(size)
                .sortBy(sortBy).sortOrder(order).build();
        return service.searchItem(params);
    }
}
