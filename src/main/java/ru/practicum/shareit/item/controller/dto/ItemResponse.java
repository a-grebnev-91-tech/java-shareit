package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
