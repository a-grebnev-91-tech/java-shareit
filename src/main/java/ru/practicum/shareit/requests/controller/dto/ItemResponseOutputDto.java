package ru.practicum.shareit.requests.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemResponseOutputDto {
    private Long itemId;
    private String name;
    private Long ownerId;
}
