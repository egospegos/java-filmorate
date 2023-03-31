package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "insert into friendship(id_user, id_friend) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "delete from friendship where id_user = ? AND id_friend = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<Long> getFriendIds(long userId) {
        String sqlQuery = "select id_friend from friendship where id_user = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFriendId, userId);
    }

    private Long mapRowToFriendId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("id_friend");
    }
}
