package ru.yandex.practicum.filmorate.service.InMemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryFilmStorage;

import java.util.List;

@Service
public class InMemoryFilmService implements FilmService {

    private final InMemoryFilmStorage storage;
    private final InMemoryUserService inMemoryUserService;

    @Autowired
    public InMemoryFilmService(InMemoryFilmStorage storage, InMemoryUserService inMemoryUserService) {
        this.storage = storage;
        this.inMemoryUserService = inMemoryUserService;
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = storage.getById(filmId);
        film.addLike(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        Film film = storage.getById(filmId);
        film.removeLike(userId);
    }

    @Override
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

    @Override
    public boolean checkUser(int id) {
        return inMemoryUserService.checkUser(id);
    }

    @Override
    public Film getFilmFromStorage(int id) {
        return storage.getById(id);
    }

    @Override
    public boolean checkFilm(int id) {
        return storage.exist(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return storage.getAll();
    }

    @Override
    public int addFilm(Film film) {
        storage.add(film);
        return storage.generateId();
    }

    @Override
    public void updateFilm(Film film) {
        storage.update(film);
    }
}
