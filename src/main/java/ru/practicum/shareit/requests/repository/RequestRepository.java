package ru.practicum.shareit.requests.repository;

import ru.practicum.shareit.requests.domain.Request;

import java.util.List;

public interface RequestRepository {
    Request save(Request model);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllRequests(Integer from, Integer size);
}
