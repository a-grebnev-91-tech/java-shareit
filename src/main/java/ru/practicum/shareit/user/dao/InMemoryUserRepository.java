package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private long currentId;
    private final Map<Long, User> users;

    public InMemoryUserRepository() {
        currentId = 1;
        users = new HashMap<>();
    }

    @Override
    public User createUser(User user) {
        long id = generateId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public Optional<User> deleteUser(long id) {
        return Optional.ofNullable(users.remove(id));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    private long generateId() {
        return currentId++;
    }
}
