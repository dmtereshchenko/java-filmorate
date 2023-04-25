package ru.yandex.practicum.filmorate.storage.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
                "left join film_category as fc on f.category_id = fc.id " +
                "left join films_and_genres fag on f.film_id = fag.film_id " +
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
        String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, " +
                "category_id = ? where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa().getId() : null,
                film.getId());
    }

    @Override
    public Optional<Film> getById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select f.film_id, f.name, f.description, f.release_date, " +
                "f.duration, f.category_id, fc.name as category_name, fg.id as genre_id, fg.name as genre_name " +
                "from films as f " +
                "left join film_category as fc on f.category_id = fc.id " +
                "left join films_and_genres fag on f.film_id = fag.film_id " +
                "left join film_genre fg on fag.genre_id = fg.id " +
                "where f.film_id = ? order by genre_id", id);
        if (filmRows.next()) {
            Film film = new Film(filmRows.getInt("film_id"), filmRows.getString("name"),
                    filmRows.getString("description"), filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"), new Mpa(filmRows.getInt("category_id"),
                    filmRows.getString("category_name")));
            if (filmRows.getInt("genre_id") > 0) {
                film.addGenre(new Genre(filmRows.getInt("genre_id"), filmRows.getString("genre_name")));
            }
            while (filmRows.next()) {
                film.addGenre(new Genre(filmRows.getInt("genre_id"), filmRows.getString("genre_name")));
            }
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

    @Override
    public List<Film> getSomeById(List<Integer> filmIds) {
        SqlParameterSource parameters = new MapSqlParameterSource("ids", filmIds);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        SqlRowSet filmRows = namedParameterJdbcTemplate.queryForRowSet("select f.film_id, f.name, f.description, f.release_date, " +
                "f.duration, f.category_id, fc.name as category_name, fag.genre_id, fg.name as genre_name from films as f " +
                "left join film_category as fc on f.category_id = fc.id " +
                "left join films_and_genres fag on f.film_id = fag.film_id " +
                "left join film_genre fg on fag.genre_id = fg.id where f.film_id in (:ids) order by film_id", parameters);
        Film film = new Film();
        List<Film> topFilms = new ArrayList<>();
        while (filmRows.next()) {
            if (filmRows.getInt("film_id") == film.getId()) {
                film.addGenre(new Genre(filmRows.getInt("genre_id"), filmRows.getString("genre_name")));
            } else {
                film = new Film(filmRows.getInt("film_id"), filmRows.getString("name"),
                        filmRows.getString("description"), filmRows.getDate("release_date").toLocalDate(),
                        filmRows.getInt("duration"), new Mpa(filmRows.getInt("category_id"),
                        filmRows.getString("category_name")));
                if (filmRows.getInt("genre_id") > 0) {
                    film.addGenre(new Genre(filmRows.getInt("genre_id"), filmRows.getString("genre_name")));
                }
                topFilms.add(film);
            }
        }
        return topFilms;
    }
}
