package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentOutputDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
