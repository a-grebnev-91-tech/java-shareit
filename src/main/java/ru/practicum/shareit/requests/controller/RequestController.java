package ru.practicum.shareit.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.controller.dto.RequestInputDto;
import ru.practicum.shareit.requests.controller.dto.RequestOutputDto;
import ru.practicum.shareit.requests.domain.RequestService;

import javax.validation.Valid;

import java.util.List;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService service;

    @PostMapping
    public RequestOutputDto createPost(
            @RequestHeader(USER_ID_HEADER) Long requesterId,
            @RequestBody @Valid RequestInputDto dto
    ) {
        log.info("User with id {} tries to crete request for item: {}", requesterId, dto.getDescription());
        return service.createRequest(requesterId, dto);
    }

    @GetMapping("/all")
    public List<RequestOutputDto> getAllRequestsButUser(
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        log.info("Obtaining all requests from {} size {} by user with id {}", from, size, userId);
        return service.getAllRequestsButUser(userId, from, size);
    }

    @GetMapping("/{id}")
    public RequestOutputDto getRequestsById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable(name = "id") Long requestId
    ) {
        log.info("User with id {} is trying to get request with id {}", userId, requestId);
        return service.getRequestById(userId, requestId);
    }

    @GetMapping
    public List<RequestOutputDto> getRequestsByUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("User with id {} is trying to get all his requests", userId);
        return service.getAllRequestsByUser(userId);
    }
}
