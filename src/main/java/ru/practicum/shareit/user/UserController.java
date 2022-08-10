package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto dto) {
        return dto;
    }


}
