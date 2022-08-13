package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.ItemService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemResponse createItem(
            @RequestHeader(value = "X-Sharer-User-Id") long userId,
            @RequestBody @Valid ItemRequest dto
    ) {
        log.info("Creating user {}", dto);
        return service.createItem(userId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemResponse patchItem(
            @RequestHeader(value = "X-Sharer-User-Id") long userId,
            @PathVariable("itemId") long itemId,
            @RequestBody ItemPatchRequest patchRequest
    ) {
        log.info("User with id {} updating {}", userId, patchRequest);

    }
}
