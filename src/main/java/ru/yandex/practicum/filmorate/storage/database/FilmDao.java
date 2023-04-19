package ru.yandex.practicum.filmorate.storage.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FilmDao implements Storage<Film> {

    private final JdbcTemplate jdbcTemplate;

    public FilmDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Film> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new Film(resultSet.getInt("film_id"), resultSet.getString("name"),
                resultSet.getString("description"), resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"), resultSet.getInt("genre_id"),
                resultSet.getInt("category_id"), resultSet.getString("category_name"));
    };
    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query("select f.film_id, f.name, f.description, f.release_date, f.duration, f. genre_id," +
                " f.category_id, fc.name as category_name from films f left join film_category fc on f.category_id = fc.id",
                ROW_MAPPER);
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
        boolean mpa = Optional.of(film.getMpa().getId()).isPresent();
        if (mpa && film.getGenres().size() > 0) {
            String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, " +
                    "category_id = ?, genre_id = ? where film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getGenres().get(0).getId(),
                    film.getId());
        } else if (mpa && film.getGenres().size() < 0) {
            String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, " +
                    "category_id = ?, where film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
        } else if (film.getGenres().size() > 0 && !mpa) {
            String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, " +
                    "genre_id = ? where film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getGenres().get(0).getId(),
                    film.getId());
        } else {
            String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ? where film_id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getId());
        }
    }

    @Override
    public Optional<Film> getById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select f.film_id, f.name, f.description, f.release_date, f.duration, f. genre_id," +
                " f.category_id, fc.name as category_name from films f left join film_category fc on f.category_id = fc.id where film_id = ?", id);
        if(filmRows.next()) {
            Film film = new Film(filmRows.getInt("film_id"), filmRows.getString("name"),
                    filmRows.getString("description"), filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"), filmRows.getInt("genre_id"),
                    filmRows.getInt("category_id"), filmRows.getString("category_name")
            );
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }
}
