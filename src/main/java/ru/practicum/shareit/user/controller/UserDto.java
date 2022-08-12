package ru.practicum.shareit.user.controller;

import lombok.*;
import ru.practicum.shareit.user.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    @NotNull
    @Email(message = "Email should be valid email")
    @UniqueEmail
    private String email;
}
