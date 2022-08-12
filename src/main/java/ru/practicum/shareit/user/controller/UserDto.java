package ru.practicum.shareit.user.controller;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    @Email(message = "Email should be valid email")
    private String email;
}
