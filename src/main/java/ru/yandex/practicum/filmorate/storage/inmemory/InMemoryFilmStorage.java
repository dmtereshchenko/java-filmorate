package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private static int filmId;

    public int generateId() {
        return ++filmId;
    }


    public List<Film> getAll() {
        List<Film> allFilms = new ArrayList<>();
        for (int id : films.keySet()) {
            allFilms.add(films.get(id));
        }
        return allFilms;
    }

    public boolean exist(int id) {
        return films.containsKey(id);
    }


    public int add(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film.getId();
    }


    public void update(Film film) {
        films.put(film.getId(), film);
    }


    public Film getById(int id) {
        return films.get(id);
    }
}