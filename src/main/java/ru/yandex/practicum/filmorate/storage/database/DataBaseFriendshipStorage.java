package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseFriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    public DataBaseFriendshipStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(int userId, int friendId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select friend_id from friendship where user_id = ?", userId);
        List<Integer> friendList = new ArrayList<>();
        if (userRows.next()) {
            friendList.add(userRows.getInt("friend_id"));
        }
        System.out.println(friendList);
        for (int i : friendList) {
            if (friendId == i) {
                break;
            }
        }
        String sqlQuery = "insert into friendship(user_id, friend_id) values (?, ?) on conflict do nothing";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }


    public void remove(int userId, int friendId) {
        String sqlQuery = "select friendship_id from friendship where user_id = ? and friend_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        if (userRows.next()) {
            System.out.println(userRows.getInt("friendship_id"));
        }
        String sqlQuery2 = "delete from friendship where friendship_id = ?";
        jdbcTemplate.update(sqlQuery2, userRows.getInt("friendship_id"));
    }


    public List<Integer> getFriendList(int id) {
        String sqlQuery = "select friend_id from friendship where user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        List<Integer> friendList = new ArrayList<>();
        while (userRows.next()) {
            friendList.add(userRows.getInt("friend_id"));
        }
        return friendList;
    }

    public List<Integer> getMutualFriendIds(int user1Id, int user2Id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select count(friend_id) as friends, friend_id from friendship " +
                "where user_id in (?, ?) group by friend_id " +
                "having friends = 2", user1Id, user2Id);
        List<Integer> mutualFriendsIds = new ArrayList<>();
        while (userRows.next()) {
            mutualFriendsIds.add(userRows.getInt("friend_id"));
        }
        return mutualFriendsIds;
    }
}
