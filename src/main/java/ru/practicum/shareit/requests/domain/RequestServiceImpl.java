package ru.practicum.shareit.requests.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.controller.dto.RequestInputDto;
import ru.practicum.shareit.requests.controller.dto.RequestOutputDto;
import ru.practicum.shareit.requests.mapper.RequestMapper;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestMapper mapper;
    private final RequestRepository requestRepository;
    @Qualifier("InDbUsers")
    private final UserRepository userRepository;

    @Override
    public RequestOutputDto createRequest(Long requesterId, RequestInputDto dto) {
        Request request = mapper.dtoToModel(dto, requesterId);
        request = requestRepository.save(request);
        return mapper.modelToDto(request);
    }

    @Override
    public List<RequestOutputDto> getAllRequestsButUser(Long userId, Integer from, Integer size) {
        checkUserExistOrThrow(userId);
        return mapper.batchModelToDto(requestRepository.findAllRequestsButUser(userId, from, size));
    }

    @Override
    public List<RequestOutputDto> getAllRequestsByUser(Long userId) {
        checkUserExistOrThrow(userId);
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return mapper.batchModelToDto(requests);
    }

    @Override
    public RequestOutputDto getRequestById(Long userId, Long requestId) {
        checkUserExistOrThrow(userId);
        Optional<Request> request = requestRepository.findById(requestId);
        return mapper.modelToDto(request.orElseThrow(
                () -> new NotFoundException(String.format("Request with id %s isn't exist", requestId)))
        );
    }

    private void checkUserExistOrThrow(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %d isn't exist", userId));
        }
    }
}
