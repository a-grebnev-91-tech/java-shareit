package ru.practicum.shareit.requests.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
public class RequestInputDto {
    @NotBlank
    private String description;
}
