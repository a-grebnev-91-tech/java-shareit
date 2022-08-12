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
}
