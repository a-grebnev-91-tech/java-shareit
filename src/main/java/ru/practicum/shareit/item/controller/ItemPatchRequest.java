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
        String header = "ItemPatchRequest{";
        StringBuilder builder = new StringBuilder(header);
        if (name != null) {
            builder.append("name='");
            builder.append(name);
            builder.append('\'');
        }
        if (description != null) {
            if (builder.length() > header.length()) {
                builder.append(", ");
            }
            builder.append("description='");
            builder.append(description);
            builder.append('\'');
        }
        if (available != null) {
            builder.append(", available='");
            builder.append(available);
            builder.append('\'');
        }
        builder.append("}");
        return builder.toString();
    }
}
