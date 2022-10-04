package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestInputDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient client;

    @PostMapping
    public ResponseEntity<Object> createPost(
            @RequestHeader(USER_ID_HEADER) @Positive long requesterId,
            @RequestBody @Valid RequestInputDto dto
    ) {
        log.info("User with id {} tries to crete request for item: {}", requesterId, dto.getDescription());
        return client.createRequest(requesterId, dto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsButUser(
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "20") @Positive int size,
            @RequestHeader(USER_ID_HEADER) @Positive long userId
    ) {
        log.info("Obtaining all requests from {} size {} by user with id {}", from, size, userId);
        return client.getAllRequestsButUser(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestsById(
            @RequestHeader(USER_ID_HEADER) @Positive long userId,
            @PathVariable(name = "id") @Positive long requestId
    ) {
        log.info("User with id {} is trying to get request with id {}", userId, requestId);
        return client.getRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(@RequestHeader(USER_ID_HEADER) @Positive long userId) {
        log.info("User with id {} is trying to get all his requests", userId);
        return client.getAllRequestsByUser(userId);
    }
}
