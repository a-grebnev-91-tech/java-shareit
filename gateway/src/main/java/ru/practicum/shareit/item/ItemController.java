package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.validation.ValidSortOrder;
import ru.practicum.shareit.validation.groups.CreateInfo;
import ru.practicum.shareit.validation.groups.PatchInfo;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.util.Constants.*;

@Slf4j
@Validated
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient client;

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @PathVariable("itemId") @Positive long itemId,
            @RequestHeader(USER_ID_HEADER) @Positive long userId,
            @RequestBody @Valid CommentInputDto comment
    ) {
        log.info("User with id {} attempt to post comment to item {}",userId, itemId);
        return client.createComment(comment, itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestHeader(value = USER_ID_HEADER) @Positive long userId,
            @RequestBody @Validated(CreateInfo.class) ItemInputDto dto
    ) {
        log.info("Creating user {}", dto);
        return client.createItem(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestHeader(value = USER_ID_HEADER) @Positive long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size,
            @RequestParam(value = "sortBy", defaultValue = ITEMS_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "order", defaultValue = ITEMS_DEFAULT_ORDER) @ValidSortOrder String order
    ) {
        log.info("Obtaining all items");
        return client.getAllByUser(userId, from, size, sortBy, order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(
            @PathVariable("id") @Positive long itemId,
            @RequestHeader(USER_ID_HEADER) @Positive long userId
    ) {
        log.info("User with id {} obtaining item with id {}", userId, itemId);
        return client.getItem(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(
            @RequestHeader(value = USER_ID_HEADER) @Positive long userId,
            @PathVariable("itemId") @Positive long itemId,
            @RequestBody @Validated(PatchInfo.class) ItemInputDto patchRequest
    ) {
        log.info("User with id {} updating {}", userId, patchRequest);
        return client.patchItem(userId, itemId, patchRequest);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam String text,
            @RequestHeader(value = USER_ID_HEADER) @Positive long userId,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size,
            @RequestParam(value = "sortBy", defaultValue = ITEMS_DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "order", defaultValue = ITEMS_DEFAULT_ORDER) @ValidSortOrder String order
    ) {
        log.info("Searching item by {}", text);
        return client.searchItem(text, userId, from, size, sortBy, order);
    }
}
