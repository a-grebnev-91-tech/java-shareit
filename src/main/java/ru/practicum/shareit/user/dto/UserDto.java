package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.user.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@UniqueEmail
public class UserDto {
    private long id;
    private String name;
    @NotBlank(message = "Email should be not blank")
    @Email(message = "Email should be valid email")
    private String email;
}
