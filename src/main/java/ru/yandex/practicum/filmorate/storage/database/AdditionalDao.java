package ru.yandex.practicum.filmorate.storage.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class AdditionalDao {

    private final JdbcTemplate jdbcTemplate;

    public AdditionalDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Mpa> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new Mpa(resultSet.getInt("id"), resultSet.getString("name"));
    };

    public List<Mpa> getAllGenres() {
        return jdbcTemplate.query("select * from film_genre", ROW_MAPPER);
    }

    public List<Mpa> getAllCategories() {
        return jdbcTemplate.query("select * from film_category", ROW_MAPPER);
    }

    public Optional<Mpa> getGenre(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from film_genre where id = ?", id);
        if(genreRows.next()) {
            Mpa genre = new Mpa(
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
}
