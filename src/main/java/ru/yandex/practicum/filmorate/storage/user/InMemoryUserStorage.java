package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements Storage<User> {

    private final HashMap<Integer, User> users = new HashMap<>();
    private static int userId;

    @Override
    public int generateId() {
        return ++userId;
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = new ArrayList<>();
        for (int id : users.keySet()) {
            allUsers.add(users.get(id));
        }
        return allUsers;
    }

    @Override
    public boolean exist(int id) {
        return users.containsKey(id);
    }

    @Override
    public void add(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }
}