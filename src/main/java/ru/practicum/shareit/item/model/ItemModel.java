package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.util.Objects;

@Getter
@Setter
public class ItemModel {
    private long id;
    private String name;
    private String description;
    private User owner;
    private boolean available;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemModel itemModel = (ItemModel) o;
        return id == itemModel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
