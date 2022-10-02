package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.groups.CreateInfo;
import ru.practicum.shareit.validation.groups.PatchInfo;

import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(CreateInfo.class) UserDto user) {
        log.info("Creating user {}", user);
        return client.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") @Positive long id) {
        log.info("Attempt to delete user with id {}", id);
        client.deleteUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Obtaining all users");
        return client.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") @Positive long id) {
        log.info("Obtaining user with id {}", id);
        return client.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUser(
            @PathVariable("id") @Positive long id,
            @RequestBody @Validated(PatchInfo.class) UserDto user
    ) {
        log.info("Updating user with id {} by {}", id, user);
        return client.patchUser(id, user);
    }
}
