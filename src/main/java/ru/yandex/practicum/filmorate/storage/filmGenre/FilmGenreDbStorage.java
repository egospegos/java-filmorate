package ru.yandex.practicum.filmorate.storage.filmGenre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;


    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void delete(long filmId) {
        String sqlQuery = "delete from film_genre where id_film = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Genre> findGenresByFilmId(Long filmId) {
        String sqlQuery = "select DISTINCT g.ID, g.NAME  from film_genre f JOIN GENRE g ON f.ID_GENRE = g.ID where id_film = ? ORDER BY g.ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmGenre, filmId);
    }

    private Genre mapRowToFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("GENRE.id"), rs.getString("GENRE.name"));
    }
}
