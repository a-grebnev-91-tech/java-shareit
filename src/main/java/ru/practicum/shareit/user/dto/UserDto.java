package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

public class UserDto {
    private long id;
    private String name;
//    @Email(message = "Email should be valid")
    private String email;

    //        @UniqueEmail(message = "Email should be unique")

}
