package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto user) {
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
        return  service.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") long id) {
        log.info("Obtaining user with id {}", id);
        return service.getUser(id);
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable("id") long id, @RequestBody @Valid UserPatchDto user) {
        log.info("Updating user with id {} by {}", id, user);
        return service.patchUser(id, user);
    }
}


