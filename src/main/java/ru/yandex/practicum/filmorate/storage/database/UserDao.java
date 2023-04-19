package ru.yandex.practicum.filmorate.storage.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserDao implements Storage<User> {
    private final JdbcTemplate jdbcTemplate;

    RowMapper<User> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
        return new User(resultSet.getInt("user_id"), resultSet.getString("login"),
                resultSet.getString("e_mail"), resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
    };

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> getById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);
        if(userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("login"),
                    userRows.getString("e_mail"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public int add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        return (int) simpleJdbcInsert.executeAndReturnKey(user.toMap());
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update users set login = ?, e_mail = ?, name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users", ROW_MAPPER);
    }
}