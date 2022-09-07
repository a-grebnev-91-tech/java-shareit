package ru.practicum.shareit.requests.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemRequestInputDto {
    @NotNull
    private String description;
}
