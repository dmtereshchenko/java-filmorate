package ru.yandex.practicum.filmorate.storage.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.util.*;

@Component
@Slf4j
public class MpaGenresDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaGenresDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Mpa> ROW_MAPPER_MPA = (ResultSet resultSet, int rowNum) -> {
        return new Mpa(resultSet.getInt("id"), resultSet.getString("name")) {
        };
    };

    RowMapper<Genre> ROW_MAPPER_GENRE = (ResultSet resultSet, int rowNum) -> {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name")) {
        };
    };

    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("select * from film_genre", ROW_MAPPER_GENRE);
    }

    public List<Mpa> getAllCategories() {
        return jdbcTemplate.query("select * from film_category", ROW_MAPPER_MPA);
    }

    public Optional<Genre> getGenre(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from film_genre where id = ?", id);
        if(genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            );
            return Optional.of(genre);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр не найден");
        }
    }

    public Optional<Mpa> getCategory(int id) {
        SqlRowSet categoryRows = jdbcTemplate.queryForRowSet("select * from film_category where id = ?", id);
        if(categoryRows.next()) {
            Mpa category = new Mpa(
                    categoryRows.getInt("id"),
                    categoryRows.getString("name")
            );
            return Optional.of(category);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Категория не найдена");
        }
    }

    public void addGenres(Film film) {
        Set<Genre> genresList = new HashSet<>();
        genresList.addAll(film.getGenres());
        String sqlQuery = "insert into films_and_genres (film_id, genre_id) values(?, ?) on conflict do nothing";
        for (Genre genre : genresList) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
        List<Integer> genresIds = getGenresByFilmId(film.getId());
        List<Genre> genres = new ArrayList<>();
        for (int i : genresIds) {
            genres.add(getGenre(i).get());
        }
        film.setGenres(genres);
    }

    public void removeGenres(int id) {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select id from films_and_genres where film_id = ?", id);
        while (genresRows.next()) {
            String sqlQuery = "delete from films_and_genres where id = ?";
            jdbcTemplate.update(sqlQuery, genresRows.getInt("id"));
        }
    }

    public List<Integer> getGenresByFilmId(int id) {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select genre_id from films_and_genres where film_id = ?", id);
        Set<Integer> genresIds = new HashSet<>();
        List<Integer> genres = new ArrayList<>();
        while (genresRows.next()) {
            genresIds.add(genresRows.getInt("genre_id"));
        }
        for (int i : genresIds) {
            genres.add(i);
        }
        return genres;
    }
}
