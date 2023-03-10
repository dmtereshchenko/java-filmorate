package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class UserStorage {

    private HashMap<Integer, User> users = new HashMap<>();
    private static int userId;

    public int generateId() {
        return ++userId;
    }

    public List<User> getUsers() {
        List<User> allUsers = new ArrayList<>();
        for (int id : users.keySet()) {
            allUsers.add(users.get(id));
        }
        return allUsers;
    }

    public void userExist(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с данным идентификатором отсутствует в базе: {}", user.getId());
            throw new ValidationException("Пользоветель с данным идентификатором отсутствует в базе.");
        }
    }

    public void addUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        users.put(user.getId(), user);
    }
}