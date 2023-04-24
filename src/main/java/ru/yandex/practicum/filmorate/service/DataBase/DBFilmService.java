package ru.yandex.practicum.filmorate.service.DataBase;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.database.DataBaseFilmStorage;
import ru.yandex.practicum.filmorate.storage.database.DataBaseLikeStorage;
import ru.yandex.practicum.filmorate.storage.database.DataBaseMpaAndGenresStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Primary
@Service
public class DBFilmService implements FilmService {

    private final DataBaseFilmStorage dao;
    private final DataBaseLikeStorage dataBaseLikeStorage;
    private final DataBaseMpaAndGenresStorage dataBaseMpaAndGenresStorage;
    private final UserService userService;

    public DBFilmService(DataBaseFilmStorage dao, DataBaseLikeStorage dataBaseLikeStorage, UserService userService, DataBaseMpaAndGenresStorage dataBaseMpaAndGenresStorage) {
        this.dao = dao;
        this.dataBaseLikeStorage = dataBaseLikeStorage;
        this.userService = userService;
        this.dataBaseMpaAndGenresStorage = dataBaseMpaAndGenresStorage;
    }

    @Override
    public void addLike(int filmId, int userId) {
        if (dao.getById(filmId).isPresent() && userService.checkUser(userId)) {
            dataBaseLikeStorage.add(filmId, userId);
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        dataBaseLikeStorage.remove(filmId, userId);
    }

    @Override
    public List<Film> topLikedFilms(int count) {
        List<Integer> topFilmsIds = dataBaseLikeStorage.getTopLikedIds(count);
        int size = topFilmsIds.size();
        if (size < count) {
            Set<Integer> topFilmsIdsSet = new HashSet<>();
            if (size > 0) {
                for (int i : topFilmsIds) {
                    topFilmsIdsSet.add(i);
                }
            }
            List<Film> allFilms = getAllFilms();
            for (Film film : allFilms) {
                topFilmsIdsSet.add(film.getId());
            }
            List<Integer> updatedTopFilmsIds = new ArrayList<>();
            for (int i : topFilmsIdsSet) {
                updatedTopFilmsIds.add(i);
            }
            while (updatedTopFilmsIds.size() > count) {
                updatedTopFilmsIds.remove(count - 1);
            }
            return dao.getSomeById(updatedTopFilmsIds);
        } else {
            return dao.getSomeById(topFilmsIds);
        }
    }

    @Override
    public boolean checkUser(int id) {
        return userService.checkUser(id);
    }

    @Override
    public Film getFilmFromStorage(int id) {
        Film film = dao.getById(id).get();
        return setFilmGenres(film);
    }

    @Override
    public boolean checkFilm(int id) {
        return dao.exists(id);
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = dao.getAll();
        List<Film> allFilmsWithGenres = new ArrayList<>();
        if (allFilms.size() > 0) {
            for (Film film : allFilms) {
                allFilmsWithGenres.add(setFilmGenres(film));
            }
        }
        return allFilmsWithGenres;
    }

    @Override
    public int addFilm(Film film) {
        int id = dao.add(film);
        film.setId(id);
        if (film.getGenres().size() > 0) {
            dataBaseMpaAndGenresStorage.addGenres(film);
        }
        return id;
    }

    @Override
    public void updateFilm(Film film) {
        dataBaseMpaAndGenresStorage.removeGenres(film.getId());
        if (film.getGenres().size() > 0) {
            dataBaseMpaAndGenresStorage.addGenres(film);
        }
        dao.update(film);
    }

    public List<Genre> getAllGenres() {
        return dataBaseMpaAndGenresStorage.getAllGenres();
    }

    public List<Mpa> getAllCategories() {
        return dataBaseMpaAndGenresStorage.getAllCategories();
    }

    public Genre getGenre(int id) {
        if (dataBaseMpaAndGenresStorage.getGenre(id).isPresent()) {
            return dataBaseMpaAndGenresStorage.getGenre(id).get();
        } else {
            return null;
        }
    }

    public Mpa getCategory(int id) {
        if (dataBaseMpaAndGenresStorage.getCategory(id).isPresent()) {
            return dataBaseMpaAndGenresStorage.getCategory(id).get();
        } else {
            return null;
        }
    }

    private Film setFilmGenres(Film film) {
        Set<Genre> genres = dataBaseMpaAndGenresStorage.getGenresByFilmId(film.getId());
        if (genres.size() > 0) {
            film.setGenres(genres);
        }
        return film;
    }
}
