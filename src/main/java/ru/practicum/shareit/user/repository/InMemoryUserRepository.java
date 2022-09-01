package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository("InMemoryUsers")
public class InMemoryUserRepository implements UserRepository {
    private long currentId;
    private final Map<Long, User> users;

    public InMemoryUserRepository() {
        currentId = 1;
        users = new HashMap<>();
    }

    @Override
    public User save(User user) {
        if (users.containsKey(user.getId())) {
            return update(user);
        } else {
            return create(user);
        }
    }

    @Override
    public Optional<User> deleteById(long id) {
        return Optional.ofNullable(users.remove(id));
    }

    @Override
    public boolean existsById(long id) {
        return users.containsKey(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    private User create(User user) {
        long id = generateId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    private long generateId() {
        return currentId++;
    }

    private User update(User user) {
        users.put(user.getId(), user);
        return user;
    }
}
