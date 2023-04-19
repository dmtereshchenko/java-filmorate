package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LikesDao {

    private final JdbcTemplate jdbcTemplate;

    public LikesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(int filmId, int userId) {
        String sqlQuery = "insert into films_likes(film_id, user_id) values (?, ?) on conflict do nothing";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void remove(int filmId, int userId) {
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select like_id from films_likes where film_id = ? and user_id = ?",
                filmId, userId);
        if (likesRows.next()) {
            int likeId = likesRows.getInt("like_id");
            String sqlQuery = "delete from films_likes where like_id = ?";
            jdbcTemplate.update(sqlQuery, likeId);
        }
    }

    public List<Integer> getTopLikedIds(int count) {
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select count(user_id), film_id from films_likes group by film_id limit ?", count);
        List<Integer> filmIds = new ArrayList<>();
        while (likesRows.next()) {
            filmIds.add(likesRows.getInt("film_id"));
        }
        return filmIds;
    }
}
