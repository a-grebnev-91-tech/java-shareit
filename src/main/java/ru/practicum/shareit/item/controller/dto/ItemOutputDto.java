package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemOutputDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentOutputDto> comments;
}
