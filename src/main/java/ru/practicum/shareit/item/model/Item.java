package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
public class Item {
    private long id;
    private String name;
    private String description;
    private User owner;
    private boolean available;
}
