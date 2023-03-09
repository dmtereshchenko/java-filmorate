package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.sevice.ValidateService;

import java.util.List;

@RestController
@Slf4j
public class UserController {

    ValidateService validator = new ValidateService();
    UserStorage storage = new UserStorage();

    @GetMapping("/users")
    List<User> findAll() {
        return storage.getUsers();
    }

    @PostMapping(value = "/users")
    User create(@Valid @RequestBody User user, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateUser(user);
        storage.addUser(user);
        return user;
    }

    @PutMapping(value = "/users")
    User updateUser(@RequestBody User user, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateUser(user);
        storage.userExist(user);
        storage.updateUser(user);
        return user;
    }
}
