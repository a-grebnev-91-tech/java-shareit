package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemResponse createItem(
            @RequestHeader(value = USER_ID_HEADER) long userId,
            @RequestBody @Valid ItemRequest dto
    ) {
        log.info("Creating user {}", dto);
        return service.createItem(userId, dto);
    }

    @GetMapping
    public List<ItemResponse> getAll(@RequestHeader(value = USER_ID_HEADER) long userId) {
        log.info("Obtaining all items");
        return service.getAllByUser(userId);
    }

    @GetMapping("/{id}")
    public ItemResponse getItem(@PathVariable("id") long id) {
        log.info("Obtaining item with id {}", id);
        return service.getItem(id);
    }

    @PatchMapping("/{itemId}")
    public ItemResponse patchItem(
            @RequestHeader(value = USER_ID_HEADER) long userId,
            @PathVariable("itemId") long itemId,
            @RequestBody ItemPatchRequest patchRequest
    ) {
        log.info("User with id {} updating {}", userId, patchRequest);
        return service.patchItem(userId, itemId, patchRequest);
    }

    @GetMapping("/search")
    public List<ItemResponse> searchItems(@RequestParam String text) {
        log.info("Searching item by {}", text);
        return service.searchItem(text);
    }
}
