package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
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

    public boolean filmExist(Film film) {
        if (films.containsKey(film.getId())) {
            return true;
        }
        return false;
    }

    public void addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    public Film getFilmById(int id) {
        return films.get(id);
    }
}