package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    ValidateService validator = new ValidateService();
    FilmStorage storage = new FilmStorage();

    @GetMapping("/films")
    public List<Film> findAll() {
        return storage.getFilms();
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateFilm(film);
        storage.addFilm(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film, HttpServletRequest request) throws ValidationException {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateFilm(film);
        storage.filmExist(film);
        storage.updateFilm(film);
        return film;
    }
}
