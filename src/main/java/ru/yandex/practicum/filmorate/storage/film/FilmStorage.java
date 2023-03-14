package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    int generateId();
    List<Film> getFilms();
    boolean filmExist(Film film);
    void addFilm(Film film);
    void updateFilm(Film film);
    Film getFilmById(int id);
}
