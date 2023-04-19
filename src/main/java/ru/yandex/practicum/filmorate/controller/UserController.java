package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.DataBase.DBUserService;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final ValidateService validator = new ValidateService();
    private final UserService service;

    @Autowired
    public UserController(DBUserService dbUserService) {
        this.service = dbUserService;
    }
    @GetMapping("/users")
    List<User> findAll() {
        return service.getAllUsers();
    }

    @GetMapping("/users/{id}/friends")
    List<User> friendList(@PathVariable int id) {
        if (!service.checkUser(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return service.returnFriendList(id);
    }

    @GetMapping("users/{id}/friends/common/{otherId}")
    List<User> commonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (!service.checkUser(id) || !service.checkUser(otherId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return service.findMutualFriends(id, otherId);
    }

    @GetMapping("users/{id}")
    User findUserById(@PathVariable int id) {
        if (!service.checkUser(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return service.getUserFromStorage(id);
    }

    @PutMapping("users/{id}/friends/{friendId}")
    void addFriend(@PathVariable int id, @PathVariable int friendId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (!service.checkUser(id) || !service.checkUser(friendId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        service.addFriend(id, friendId);
    }

    @DeleteMapping("users/{id}/friends/{friendId}")
    void deleteFriend(@PathVariable int id, @PathVariable int friendId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (!service.checkUser(id) || !service.checkUser(friendId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        service.deleteFriend(id, friendId);
    }

    @PutMapping(value = "/users")
    User updateUser(@RequestBody User user, HttpServletRequest request) {
        if (!service.checkUser(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateUser(user);
        service.updateUser(user);
        return user;
    }

    @PostMapping(value = "/users")
    User create(@Valid @RequestBody User user, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateUser(user);
        user.setId(service.addUser(user));
        return user;
    }
}