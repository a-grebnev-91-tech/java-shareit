package ru.practicum.shareit.user.controller;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private String name;
    //TODO исправить после завершения проекта
//    @UniqueEmail // constraint passed to database for YP postman's tests
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
