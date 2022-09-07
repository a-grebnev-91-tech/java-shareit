package ru.practicum.shareit.requests.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemRequestOutputDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponseOutputDto> items;
}
