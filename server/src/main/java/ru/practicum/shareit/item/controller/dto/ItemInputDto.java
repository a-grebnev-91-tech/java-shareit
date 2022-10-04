package ru.practicum.shareit.item.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemInputDto {
    private Boolean available;
    private String description;
    private String name;
    private Long requestId;

    @Override
    public String toString() {
        String header = "ItemRequest{";
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
            if (builder.length() > header.length()) {
                builder.append(", ");
            }
            builder.append("available='");
            builder.append(available);
            builder.append('\'');
        }
        builder.append("}");
        return builder.toString();
    }
}
