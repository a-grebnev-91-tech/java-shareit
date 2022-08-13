package ru.practicum.shareit.item.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemRequest {
    @NotBlank(message = "name should not be blank")
    private String name;
    @NotBlank(message = "description should not be blank")
    private String description;
    private Long ownerId;
    @NotNull(message = "available should not be null")
    private Boolean available;
}
