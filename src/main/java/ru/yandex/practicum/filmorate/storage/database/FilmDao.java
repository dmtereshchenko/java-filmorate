package ru.yandex.practicum.filmorate.storage.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;

@Component
@Slf4j
public class FilmDao implements Storage<Film> {

    private final JdbcTemplate jdbcTemplate;

    public FilmDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select film_id from films");
        List <Film> allFilms = new ArrayList<>();
        Set<Integer> allFilmsIds = new TreeSet<>();
        while (filmRows.next()) {
            allFilmsIds.add(filmRows.getInt("film_id"));
        }
        for (int i : allFilmsIds) {
            allFilms.add(getById(i).get());
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
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where film_id = ?", id);
        if(filmRows.next()) {
            Film film = new Film(filmRows.getInt("film_id"), filmRows.getString("name"),
                    filmRows.getString("description"), filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration")
            );
            SqlRowSet mpaIdRows = jdbcTemplate.queryForRowSet("select category_id from films where film_id = ?", id);
            if (mpaIdRows.next()) {
                int mpaId = mpaIdRows.getInt("category_id");
                if (mpaId > 0) {
                    SqlRowSet mpaNameRows = jdbcTemplate.queryForRowSet("select name from film_category where id = ?", mpaId);
                    if (mpaNameRows.next()) {
                        film.setMpa(new Mpa(mpaId, mpaNameRows.getString("name")));
                    }
                }
            }
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}
