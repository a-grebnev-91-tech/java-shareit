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
    public List<RequestOutputDto> getAllRequestsByUser(Long userId) {
        checkUserExisting(userId);
        return mapper.batchModelToDto(requestRepository.findAllByRequesterId(userId));
    }

    private void checkUserExisting(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %d isn't exist", userId));
        }
    }
}
