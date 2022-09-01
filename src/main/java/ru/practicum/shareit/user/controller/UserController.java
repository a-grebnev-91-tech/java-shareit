package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserService;
import ru.practicum.shareit.util.validation.groups.CreateInfo;
import ru.practicum.shareit.util.validation.groups.PatchInfo;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto createUser(@RequestBody @Validated(CreateInfo.class) UserDto user) {
        log.info("Creating user {}", user);
        return service.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        log.info("Attempt to delete user with id {}", id);
        service.deleteUser(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Obtaining all users");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") long id) {
        log.info("Obtaining user with id {}", id);
        return service.getUser(id);
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable("id") long id, @RequestBody @Validated(PatchInfo.class) UserDto user) {
        log.info("Updating user with id {} by {}", id, user);
        return service.patchUser(id, user);
    }
}


