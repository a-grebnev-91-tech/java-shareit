package ru.practicum.shareit.requests.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.requests.controller.dto.RequestInputDto;
import ru.practicum.shareit.requests.controller.dto.RequestOutputDto;
import ru.practicum.shareit.requests.mapper.RequestMapper;
import ru.practicum.shareit.requests.repository.RequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestMapper mapper;
    private final RequestRepository requestRepository;

    @Override
    public RequestOutputDto createRequest(Long requesterId, RequestInputDto dto) {
        Request request = mapper.dtoToModel(dto, requesterId);
        request = requestRepository.save(request);
        return mapper.modelToDto(request);
    }

    @Override
    public List<RequestOutputDto> getRequestByUser(Long userId) {
        return null;
    }


}
