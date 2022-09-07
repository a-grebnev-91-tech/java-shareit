package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CommentInputDto {
    @NotEmpty
    private String text;
}
