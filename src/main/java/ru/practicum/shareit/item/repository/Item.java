package ru.practicum.shareit.item.repository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private long id;
    private String name;
    private String description;
    private long ownerId;
    private boolean available;
}
