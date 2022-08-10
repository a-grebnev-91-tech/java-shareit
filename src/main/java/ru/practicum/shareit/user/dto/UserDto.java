package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.user.validation.UniqueEmail;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserDto {
    private long id;
    private String name;
    @Email(message = "Email should be valid")
    @UniqueEmail(message = "Email should be unique")
    private String email;


}
