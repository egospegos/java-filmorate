package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate, FilmGenreStorage filmGenreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreStorage = filmGenreStorage;
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

    @Override
    public List<Film> getPopular(int count) {
        final String sqlQuery = "SELECT COUNT(ID_FILM), f.id, f.NAME, f.description, f.release_date, f.duration, m.id, m.name " +
                "FROM LIKES JOIN FILM f ON LIKES.ID_FILM = f.ID JOIN MPA m ON f.ID_MPA = m.ID GROUP BY ID_FILM LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genreList = filmGenreStorage.findGenresByFilmId(rs.getLong("FILM.id"));

        return new Film(rs.getLong("FILM.id"), new MPA(rs.getLong("MPA.id"), rs.getString("MPA.name")),
                rs.getString("FILM.name"), rs.getString("FILM.description"),
                rs.getDate("FILM.release_date").toLocalDate(), rs.getInt("FILM.duration"), genreList);
    }

}
