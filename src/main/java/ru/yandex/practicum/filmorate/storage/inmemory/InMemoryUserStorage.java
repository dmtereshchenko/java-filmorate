package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private static int userId;

    public int generateId() {
        return ++userId;
    }

    public List<User> getAll() {
        List<User> allUsers = new ArrayList<>();
        for (int id : users.keySet()) {
            allUsers.add(users.get(id));
        }
        return allUsers;
    }

    public boolean exist(int id) {
        return users.containsKey(id);
    }

    public int add(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user.getId();
    }

    public void update(User user) {
        users.put(user.getId(), user);
    }

    public User getById(int id) {
        return users.get(id);
    }
}