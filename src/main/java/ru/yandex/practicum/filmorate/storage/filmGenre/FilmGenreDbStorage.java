package ru.yandex.practicum.filmorate.storage.filmGenre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public void updateGenresOfFilm(Long filmId, List<Genre> genres) {
        //удалить старые жанры у фильма
        delete(filmId);

        if (genres.size() != 0) {
            for (Genre genre : genres) {
                String sqlQuery = "insert into film_genre(id_film, id_genre) " +
                        "values (?, ?)";
                jdbcTemplate.update(sqlQuery,
                        filmId,
                        genre.getId());
            }
        }
    }

    @Override
    public void delete(long filmId) {
        String sqlQuery = "delete from film_genre where id_film = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Genre> findGenresByFilmId(Long filmId) {
        String sqlQuery = "select DISTINCT id_genre from film_genre where id_film = ? ORDER BY ID_GENRE";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmGenre, filmId);
    }

    private Genre mapRowToFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        return genreStorage.get(rs.getLong("id_genre"));
    }
}
