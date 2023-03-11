package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final ValidateService validator = new ValidateService();
    private final UserStorage storage = new UserStorage();

    @GetMapping("/users")
    List<User> findAll() {
        return storage.getUsers();
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
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateUser(user);
        storage.userExist(user);
        storage.updateUser(user);
        return user;
    }
}