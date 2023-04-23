package ru.yandex.practicum.filmorate.storage.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class DataBaseFilmStorage implements Storage<Film> {

    private final JdbcTemplate jdbcTemplate;

    public DataBaseFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select f.film_id, f.name, f.description, f.release_date, " +
                "f.duration, f.category_id, fc.name as category_name, fag.genre_id, fg.name as genre_name from films as f " +
                "left join film_category as fc on f.category_id = fc.id left join films_and_genres fag on f.film_id = fag.film_id " +
                "left join film_genre fg on fag.genre_id = fg.id ");
        List<Film> allFilms = new ArrayList<>();
        int id = 0;
        Film film = new Film();
        while (filmRows.next()) {
            if (filmRows.getInt("film_id") == id) {
                film.addGenre(new Genre(filmRows.getInt("genre_id"), filmRows.getString("genre_name")));
            } else {
                film = new Film(filmRows.getInt("film_id"), filmRows.getString("name"),
                        filmRows.getString("description"), filmRows.getDate("release_date").toLocalDate(),
                        filmRows.getInt("duration"), new Mpa(filmRows.getInt("category_id"),
                        filmRows.getString("category_name")));
                if (filmRows.getInt("genre_id") > 0) {
                    film.addGenre(new Genre(filmRows.getInt("genre_id"), filmRows.getString("genre_name")));
                }
                id = film.getId();
                allFilms.add(film);
            }
        }
        return allFilms;
    }

    @Override
    public int add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        return (int) simpleJdbcInsert.executeAndReturnKey(film.toMap());
    }

    @Override
    public void update(Film film) {
        if (Optional.of(film.getMpa().getId()).isPresent()) {
            String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, " +
                    "category_id = ? where film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
        } else {
            String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, " +
                    "category_id = ? where film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    null,
                    film.getId());
        }
    }

    @Override
    public Optional<Film> getById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select f.film_id, f.name, f.description, f.release_date, " +
                "f.duration, f.category_id, fc.name as category_name from films as f left join film_category as fc on f.category_id = fc.id " +
                "where film_id = ?", id);
        if (filmRows.next()) {
            Film film = new Film(filmRows.getInt("film_id"), filmRows.getString("name"),
                    filmRows.getString("description"), filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"), new Mpa(filmRows.getInt("category_id"),
                    filmRows.getString("category_name")));
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public boolean exists(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select exists(select film_id from films where film_id = ?) as exist", id);
        if (filmRows.next()) {
            return filmRows.getBoolean("exist");
        } else {
            return false;
        }
    }
}
