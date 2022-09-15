package ru.practicum.shareit.requests.domain;

import ru.practicum.shareit.requests.controller.dto.RequestInputDto;
import ru.practicum.shareit.requests.controller.dto.RequestOutputDto;

import java.util.List;

public interface RequestService {
    RequestOutputDto createRequest(Long requesterId, RequestInputDto dto);

    List<RequestOutputDto> getAllRequestsButUser(Long userId, Integer from, Integer size);

    List<RequestOutputDto> getAllRequestsByUser(Long userId);

    RequestOutputDto getRequestById(Long userId, Long requestId);
}
