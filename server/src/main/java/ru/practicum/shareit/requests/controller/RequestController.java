package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.controller.dto.RequestInputDto;
import ru.practicum.shareit.requests.controller.dto.RequestOutputDto;
import ru.practicum.shareit.requests.domain.RequestService;

import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService service;

    @PostMapping
    public RequestOutputDto createPost(
            @RequestHeader(USER_ID_HEADER) long requesterId,
            @RequestBody RequestInputDto dto
    ) {
        log.info("User with id {} tries to crete request for item: {}", requesterId, dto.getDescription());
        return service.createRequest(requesterId, dto);
    }

    @GetMapping("/all")
    public List<RequestOutputDto> getAllRequestsButUser(
            @RequestParam(name = "from") int from,
            @RequestParam(name = "size") int size,
            @RequestHeader(USER_ID_HEADER) long userId
    ) {
        log.info("Obtaining all requests from {} size {} by user with id {}", from, size, userId);
        return service.getAllRequestsButUser(userId, from, size);
    }

    @GetMapping("/{id}")
    public RequestOutputDto getRequestsById(
            @RequestHeader(USER_ID_HEADER) long userId,
            @PathVariable(name = "id") long requestId
    ) {
        log.info("User with id {} is trying to get request with id {}", userId, requestId);
        return service.getRequestById(userId, requestId);
    }

    @GetMapping
    public List<RequestOutputDto> getRequestsByUser(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("User with id {} is trying to get all his requests", userId);
        return service.getAllRequestsByUser(userId);
    }
}

