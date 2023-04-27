package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> topLikedFilms(int count);

    boolean checkUser(int id);

    Film getFilmFromStorage(int id);

    boolean checkFilm(int id);

    List<Film> getAllFilms();

    int addFilm(Film film);

    void updateFilm(Film film);
}
