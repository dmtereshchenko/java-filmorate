package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements Storage<Film> {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private static int filmId;

    @Override
    public int generateId() {
        return ++filmId;
    }

    @Override
    public List<Film> getAll() {
        List<Film> allFilms = new ArrayList<>();
        for (int id : films.keySet()) {
            allFilms.add(films.get(id));
        }
        return allFilms;
    }

    @Override
    public boolean exist(int id) {
        return films.containsKey(id);
    }

    @Override
    public void add(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }
}