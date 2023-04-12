package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.User.InMemoryUserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final InMemoryFilmStorage storage;
    private final InMemoryUserService inMemoryUserService;

    @Autowired
    public FilmService(InMemoryFilmStorage storage, InMemoryUserService inMemoryUserService) {
        this.storage = storage;
        this.inMemoryUserService = inMemoryUserService;
    }

    public void addLike(int filmId, int userId) {
        Film film = storage.getById(filmId);
        film.addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = storage.getById(filmId);
        film.removeLike(userId);
    }

    public List<Film> topLikedFilms(int count) {
            List<Film> allFilms = storage.getAll();
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

    public boolean checkUser(int id){
        return inMemoryUserService.checkUser(id);
    }

    public Film getFilmFromStorage(int id) {
        return storage.getById(id);
    }

    public boolean checkFilm(int id) {
        return storage.exist(id);
    }

    public List<Film> getAllFilms() {
        return storage.getAll();
    }

    public void addFilm(Film film) {
        storage.add(film);
    }

    public void updateFilm(Film film) {
        storage.update(film);
    }
}
