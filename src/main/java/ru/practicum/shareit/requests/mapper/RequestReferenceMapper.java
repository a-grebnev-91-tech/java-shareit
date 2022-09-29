package ru.practicum.shareit.requests.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.domain.Request;
import ru.practicum.shareit.requests.repository.RequestRepository;

@Component
@RequiredArgsConstructor
public class RequestReferenceMapper {
    private final RequestRepository repository;

    public Request idToRequest(Long id) {
        if (id == null) return null;
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Request with id %d isn't exist", id))
        );
    }

    public Long requestToId(Request request) {
        if (request == null) return null;
        return request.getId();
    }
}
