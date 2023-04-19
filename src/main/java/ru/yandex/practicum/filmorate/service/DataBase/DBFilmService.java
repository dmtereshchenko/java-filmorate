package ru.yandex.practicum.filmorate.service.DataBase;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.database.FilmDao;
import ru.yandex.practicum.filmorate.storage.database.LikesDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Primary
@Service
public class DBFilmService implements FilmService {

    private final FilmDao dao;
    private final LikesDao likesDao;
    private final UserService userService;

    public DBFilmService(FilmDao dao, LikesDao likesDao, UserService userService) {
        this.dao = dao;
        this.likesDao = likesDao;
        this.userService = userService;
    }

    @Override
    public void addLike(int filmId, int userId) {
        if (dao.getById(filmId).isPresent() && userService.checkUser(userId)) {
            likesDao.add(filmId, userId);
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        likesDao.remove(filmId, userId);
    }

    @Override
    public List<Film> topLikedFilms(int count) {
        List<Integer> topFilmsIds = likesDao.getTopLikedIds(count);
        int size = topFilmsIds.size();
        List<Film> topFilms = new ArrayList<>();
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
                updatedTopFilmsIds.remove(count-1);
            }
            for (int i : updatedTopFilmsIds) {
                topFilms.add(getFilmFromStorage(i));
            }
        } else {
            for (int i : topFilmsIds) {
                topFilms.add(getFilmFromStorage(i));
            }
        }
        return topFilms;
    }

    @Override
    public boolean checkUser(int id) {
        return userService.checkUser(id);
    }

    @Override
    public Film getFilmFromStorage(int id) {
            Film film = dao.getById(id).get();
            if (film.getGenres().size() > 0) {
                if (film.getGenres().get(0).getId() == 0) {
                    List<Genre> genres = new ArrayList<Genre>();
                    film.setGenres(genres);
                }
            }
            return film;
    }

    @Override
    public boolean checkFilm(int id) {
        return dao.getById(id).isPresent();
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = dao.getAll();
        for (Film film : allFilms) {
            if (film.getGenres().size() > 0) {
                if (film.getGenres().get(0).getId() == 0) {
                    List<Genre> genres = new ArrayList<Genre>();
                    film.setGenres(genres);
                }
            }
        }
        return allFilms;
    }

    @Override
    public int addFilm(Film film) {
        return dao.add(film);
    }

    @Override
    public void updateFilm(Film film) {
        dao.update(film);
    }
}
