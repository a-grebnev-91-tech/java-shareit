package ru.practicum.shareit.requests.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.controller.dto.ItemOutputDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class RequestOutputDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemOutputDto> items;
}
