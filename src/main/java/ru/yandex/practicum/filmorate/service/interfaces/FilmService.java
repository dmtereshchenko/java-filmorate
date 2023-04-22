package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    public void addLike(int filmId, int userId);

    public void removeLike(int filmId, int userId);

    public List<Film> topLikedFilms(int count);

    public boolean checkUser(int id);

    public Film getFilmFromStorage(int id);

    public boolean checkFilm(int id);

    public List<Film> getAllFilms();

    public int addFilm(Film film);

    public void updateFilm(Film film);
}
