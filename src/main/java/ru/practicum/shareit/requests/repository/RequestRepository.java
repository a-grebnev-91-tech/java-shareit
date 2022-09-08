package ru.practicum.shareit.requests.repository;

import ru.practicum.shareit.requests.domain.Request;

public interface RequestRepository {
    Request save(Request model);
}
