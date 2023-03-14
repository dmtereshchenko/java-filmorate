package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final ValidateService validator = new ValidateService();
    private final UserStorage storage;
    private final UserService service;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.storage = userStorage;
        this.service = userService;
    }
    @GetMapping("/users")
    List<User> findAll() {
        return storage.getUsers();
    }

    @GetMapping("/users/{id}/friends")
    List<User> friendList(@PathVariable int id) {
        if (storage.getUserById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return service.returnFriendList(id);
    }

    @GetMapping("users/{id}/friends/common/{otherId}")
    List<User> commonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (storage.getUserById(id) == null || storage.getUserById(otherId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return service.findMutualFriends(id, otherId);
    }

    @GetMapping("users/{id}")
    User findUserById(@PathVariable int id) {
        if (storage.getUserById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return storage.getUserById(id);
    }

    @PutMapping("users/{id}/friends/{friendId}")
    void addFriend(@PathVariable int id, @PathVariable int friendId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (storage.getUserById(id) == null || storage.getUserById(friendId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        service.addFriend(id, friendId);
    }

    @DeleteMapping("users/{id}/friends/{friendId}")
    void deleteFriend(@PathVariable int id, @PathVariable int friendId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (storage.getUserById(id) == null || storage.getUserById(friendId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        service.deleteFriend(id, friendId);
    }

    @PostMapping(value = "/users")
    User create(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateUser(user);
        storage.addUser(user);
        return user;
    }

    @PutMapping(value = "/users")
    User updateUser(@RequestBody User user, HttpServletRequest request) {
        if (storage.getUserById(user.getId()) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateUser(user);
        storage.userExist(user);
        storage.updateUser(user);
        return user;
    }
}