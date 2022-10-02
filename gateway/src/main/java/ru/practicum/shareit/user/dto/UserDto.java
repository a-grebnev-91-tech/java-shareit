package ru.practicum.shareit.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validation.groups.CreateInfo;
import ru.practicum.shareit.validation.groups.PatchInfo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@EqualsAndHashCode
public class UserDto {
    @Null(groups = PatchInfo.class)
    private Long id;
    @NotBlank(groups = CreateInfo.class)
    private String name;
    @NotNull(groups = CreateInfo.class, message = "email should not be null")
    @Email(groups = {CreateInfo.class, PatchInfo.class}, message = "email should be valid email address")
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
