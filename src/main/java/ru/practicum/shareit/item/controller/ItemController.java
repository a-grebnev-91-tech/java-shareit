package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.controller.dto.CommentInputDto;
import ru.practicum.shareit.item.controller.dto.CommentOutputDto;
import ru.practicum.shareit.item.controller.dto.ItemInputDto;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;
import ru.practicum.shareit.item.domain.ItemService;
import ru.practicum.shareit.util.validation.groups.CreateInfo;
import ru.practicum.shareit.util.validation.groups.PatchInfo;

import javax.validation.Valid;
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
            @RequestBody @Valid CommentInputDto comment
    ) {
        log.info("User with id {} attempt to post comment to item {}",userId, itemId);
        return service.createComment(comment, itemId, userId);
    }

    @PostMapping
    public ItemOutputDto createItem(
            @RequestHeader(value = USER_ID_HEADER) long userId,
            @RequestBody @Validated(CreateInfo.class) ItemInputDto dto
    ) {
        log.info("Creating user {}", dto);
        return service.createItem(userId, dto);
    }

    @GetMapping
    public List<ItemOutputDto> getAll(@RequestHeader(value = USER_ID_HEADER) long userId) {
        log.info("Obtaining all items");
        return service.getAllByUser(userId);
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
            @RequestBody @Validated(PatchInfo.class) ItemInputDto patchRequest
    ) {
        log.info("User with id {} updating {}", userId, patchRequest);
        return service.patchItem(userId, itemId, patchRequest);
    }

    @GetMapping("/search")
    public List<ItemOutputDto> searchItems(@RequestParam String text) {
        log.info("Searching item by {}", text);
        return service.searchItem(text);
    }
}
