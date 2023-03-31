package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.MPA.MPAStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MPAStorage mpaStorage;
    private final FilmGenreStorage filmGenreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MPAStorage mpaStorage, FilmGenreStorage filmGenreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.filmGenreStorage = filmGenreStorage;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into film(name, description, release_date, duration, id_mpa) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());

        //делаем записи с информацией о жанрах фильма
        filmGenreStorage.updateGenresOfFilm(film.getId(), film.getGenres());

        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update film set " +
                "name = ?, description = ?, release_date = ?, duration = ?, id_mpa = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        //делаем записи с информацией о жанрах фильма
        filmGenreStorage.updateGenresOfFilm(film.getId(), film.getGenres());
        return get(film.getId());
    }

    @Override
    public Film get(long id) {
        String sqlQuery = "select id, name, description, release_date, duration, id_mpa " +
                "from film where id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException("Пользователь с таким id не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from film where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "select id, name, description, release_date, duration, id_mpa from film";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genreList = filmGenreStorage.findGenresByFilmId(rs.getLong("id"));

        return new Film(rs.getLong("id"), mpaStorage.findMpaById(rs.getLong("id_mpa")),
                rs.getString("name"), rs.getString("description"),
                rs.getDate("release_date").toLocalDate(), rs.getInt("duration"), genreList);
    }
}
