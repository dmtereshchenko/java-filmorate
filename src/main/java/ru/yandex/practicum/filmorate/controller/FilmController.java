package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final ValidateService validator = new ValidateService();
    private final FilmService service;

    @Autowired
    public FilmController(FilmService filmService) {
        this.service = filmService;
    }

    @GetMapping("/films")
     List<Film> findAll() {
        return service.getAllFilms();
    }

    @GetMapping("/films/popular")
    @ResponseBody
    List<Film> findTopFilms(@RequestParam(defaultValue = "10") int count) {
        List<Film> topFilms = service.topLikedFilms(count);
        return topFilms;
    }

    @GetMapping("/films/{id}")
    Film findFilmById(@PathVariable int id) {
        if (!service.checkFilm(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        return service.getFilmFromStorage(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    void likeFilm(@PathVariable int id, @PathVariable int userId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (!service.checkFilm(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        } else if (!service.checkUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        service.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    void deleteLike(@PathVariable int id, @PathVariable int userId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (!service.checkFilm(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        } else if (!service.checkUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        service.removeLike(id, userId);
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateFilm(film);
        service.addFilm(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (!service.checkFilm(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        validator.validateFilm(film);
        service.updateFilm(film);
        return film;
    }
}
