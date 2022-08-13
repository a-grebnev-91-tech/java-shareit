package ru.practicum.shareit.item.controller;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.validation.NotBlankButNull;

@Getter
@Setter
public class ItemPatchRequest {
    @NotBlankButNull
    private String name;
    @NotBlankButNull
    private String description;
    @NotBlankButNull
    private Boolean available;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("ItemPatchRequest{");
        if (name != null) {
            builder.append("name='");
            builder.append(name);
            builder.append('\'');
            builder.append(", ");
        }
        if (description != null) {
            builder.append("description='");
            builder.append(description);
            builder.append('\'');
            builder.append(", ");
        }
        if (available != null) {
            builder.append("available='");
            builder.append(available);
            builder.append('\'');
            builder.append(", ");
        }
        builder.append("}");
        return builder.toString();
    }
}
