package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.entity.User;

import java.util.List;

@Repository
public class InMemoryUserRepository implements UserRepository {

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User deleteUser(long id) {
        return null;
    }

    @Override
    public boolean emailIsAvailable(long userId, String email) {
        return userId == 0 && email.equals("user@user.com");
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }
}
