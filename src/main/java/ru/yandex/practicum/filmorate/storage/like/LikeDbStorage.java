package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "insert into likes(id_film, id_user) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQuery = "delete from likes where id_film = ? AND id_user = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public Integer countFilmLikes(long filmId) {
        String sqlQuery = "SELECT COUNT(ID_FILM) FROM LIKES WHERE ID_FILM = ?";
        return jdbcTemplate.queryForObject(sqlQuery, new Object[]{filmId}, Integer.class);
    }
}
