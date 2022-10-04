package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validation.NotBlankButNull;
import ru.practicum.shareit.validation.groups.CreateInfo;
import ru.practicum.shareit.validation.groups.PatchInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ItemInputDto {
    @NotNull(groups = CreateInfo.class, message = "available should not be null")
    private Boolean available;
    @NotBlank(groups = CreateInfo.class, message = "description should not be blank")
    @NotBlankButNull(groups = PatchInfo.class)
    private String description;
    @NotBlank(groups = CreateInfo.class, message = "name should not be blank")
    @NotBlankButNull(groups = PatchInfo.class)
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
