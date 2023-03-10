package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class FilmStorage {

    private HashMap<Integer, Film> films = new HashMap<>();
    private static int filmId;

    public int generateId() {
        return ++filmId;
    }

    public List<Film> getFilms() {
        List<Film> allFilms = new ArrayList<>();
        for (int id : films.keySet()) {
            allFilms.add(films.get(id));
        }
        return allFilms;
    }

    public void filmExist(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с данным идентификатором отсутствует в базе: {}", film.getId());
            throw new ValidationException("Фильм с данным идентификатором отсутствует в базе.");
        }
    }

    public void addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }
}