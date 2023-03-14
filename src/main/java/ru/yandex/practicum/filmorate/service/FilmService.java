package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public void addLike(int filmId, int userId) {
        Film film = storage.getFilmById(filmId);
        film.addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = storage.getFilmById(filmId);
        film.removeLike(userId);
    }

    public List<Film> topLikedFilms(int count) {
        Optional<Integer> o = Optional.of(count);
        if (o.isEmpty()) {
            count = 10;
        }
        List<Film> allFilms = storage.getFilms();
        while (allFilms.size() > count) {
            Film lowestLiked = null;
            for (Film film : allFilms) {
                if (null == lowestLiked || film.compareTo(lowestLiked) < 0) {
                    lowestLiked = film;
                }
                allFilms.remove(lowestLiked);
            }
        }
        return allFilms;
    }
}
