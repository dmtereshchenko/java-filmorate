package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final ValidateService validator = new ValidateService();
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final FilmService service = new FilmService(filmStorage);

    @Autowired
    public FilmController(UserStorage storage) {
        this.userStorage = storage;
    }

    @GetMapping("/films")
     List<Film> findAll() {
        return filmStorage.getFilms();
    }

    @GetMapping("/films/popular")
    List<Film> findTopFilms(@RequestParam(required = false) int count) {
        return service.topLikedFilms(count);
    }

    @GetMapping("/films/{id}")
    Film findFilmById(@PathVariable int id) {
        if (filmStorage.getFilmById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        return filmStorage.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    void likeFilm(@PathVariable int id, @PathVariable int userId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (filmStorage.getFilmById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        service.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    void deleteLike(@PathVariable int id, @PathVariable int userId, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (filmStorage.getFilmById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        } else if  {

        }
        service.removeLike(id, userId);
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        validator.validateFilm(film);
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: {}, Строка параметров запроса: {}", request.getRequestURI(), request.getQueryString());
        if (!filmStorage.filmExist(film)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }
        validator.validateFilm(film);
        filmStorage.filmExist(film);
        filmStorage.updateFilm(film);
        return film;
    }
}
