package ru.practicum.shareit.user.controller;

import lombok.*;
import ru.practicum.shareit.util.validation.groups.CreateInfo;
import ru.practicum.shareit.util.validation.groups.PatchInfo;
import ru.practicum.shareit.util.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
public class UserDto {
    @Null(groups = PatchInfo.class)
    private Long id;
    @NotBlank(groups = CreateInfo.class)
    private String name;
    @NotNull(groups = CreateInfo.class, message = "email should not be null")
    @Email(groups = {CreateInfo.class, PatchInfo.class}, message = "email should be valid email address")
    @UniqueEmail(groups = {CreateInfo.class, PatchInfo.class})
    private String email;

    @Override
    public String toString() {
        String header = "UserDto{";
        StringBuilder builder = new StringBuilder(header);
        if (id != null) {
            builder.append("id='");
            builder.append(id);
            builder.append('\'');
        }
        if (name != null) {
            if (builder.length() > header.length()) {
                builder.append(", ");
            }
            builder.append("name='");
            builder.append(name);
            builder.append('\'');
        }
        if (email != null) {
            if (builder.length() > header.length()) {
                builder.append(", ");
            }
            builder.append("email='");
            builder.append(email);
            builder.append('\'');
        }
        builder.append("}");
        return builder.toString();
    }
}
