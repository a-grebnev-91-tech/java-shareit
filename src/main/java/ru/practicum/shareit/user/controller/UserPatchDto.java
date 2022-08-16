package ru.practicum.shareit.user.controller;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.validation.UniqueEmail;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserPatchDto {
    private String name;
    @Email(message = "Email should be valid email")
    @UniqueEmail
    private String email;

    @Override
    public String toString() {
        String header = "UserPatchDto{";
        StringBuilder builder = new StringBuilder(header);
        if (name != null) {
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
            builder.append(", ");
        }
        builder.append("}");
        return builder.toString();
    }
}
