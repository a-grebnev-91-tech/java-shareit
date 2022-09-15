package ru.practicum.shareit.requests.repository;

import ru.practicum.shareit.requests.domain.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository {
    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllRequests(Integer from, Integer size);

    Optional<Request> findById(Long id);

    Request save(Request model);
}
