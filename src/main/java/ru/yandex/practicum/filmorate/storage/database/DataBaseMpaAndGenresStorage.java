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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
public class DataBaseMpaAndGenresStorage {

    private final JdbcTemplate jdbcTemplate;
    RowMapper<Mpa> mpaRowMapper = (ResultSet resultSet, int rowNum) -> {
        return new Mpa(resultSet.getInt("id"), resultSet.getString("name")) {
        };
    };
    RowMapper<Genre> genreRowMapper = (ResultSet resultSet, int rowNum) -> {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name")) {
        };
    };

    public DataBaseMpaAndGenresStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("select * from film_genre", genreRowMapper);
    }

    public List<Mpa> getAllCategories() {
        return jdbcTemplate.query("select * from film_category", mpaRowMapper);
    }

    public Optional<Genre> getGenre(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from film_genre where id = ?", id);
        if (genreRows.next()) {
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
        if (categoryRows.next()) {
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
        Set<Genre> genresList = film.getGenres();
        String sqlQuery = "insert into films_and_genres (film_id, genre_id) values(?, ?) on conflict do nothing";
        for (Genre genre : genresList) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
        film.setGenres(getGenresByFilmId(film.getId()));
    }

    public void removeGenres(int id) {
        String sqlQuery = "delete from films_and_genres where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public Set<Genre> getGenresByFilmId(int id) {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select fag.genre_id, fg.name from films_and_genres fag " +
                "left join film_genre fg on fag.genre_id = fg.id where film_id = ? order by genre_id", id);
        Set<Genre> genres = new HashSet<>();
        while (genresRows.next()) {
            genres.add(new Genre(genresRows.getInt("genre_id"), genresRows.getString("name")));
        }
        return genres;
    }

    public boolean genreExists(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select exists(select id from film_genre where id = ?) as exist", id);
        if (genreRows.next()) {
            return genreRows.getBoolean("exist");
        } else {
            return false;
        }
    }

    public boolean categoryExists(int id) {
        SqlRowSet categoryRows = jdbcTemplate.queryForRowSet("select exists(select id from film_category where id = ?) as exist", id);
        if (categoryRows.next()) {
            return categoryRows.getBoolean("exist");
        } else {
            return false;
        }
    }
}
