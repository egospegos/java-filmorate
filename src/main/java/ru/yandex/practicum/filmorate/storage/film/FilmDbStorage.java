package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenreStorage filmGenreStorage) {
        this.jdbcTemplate = jdbcTemplate;
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
        updateGenresOfFilm(film.getId(), film.getGenres());

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
        updateGenresOfFilm(film.getId(), film.getGenres());
        return get(film.getId());
    }

    @Override
    public Film get(long id) {
        String sqlQuery = "select f.id, f.name, f.description, f.release_date, f.duration, MPA.ID, MPA.name  " +
                "from film f JOIN MPA ON f.ID_MPA = MPA.ID where f.id = ?";
        Film film = new Film();
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException("Пользователь с таким id не найден");
        }
        return film;
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "delete from film where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<Film> getAll() {
        //String sqlQuery = "select id, name, description, release_date, duration, id_mpa from film";
        String sqlQuery = "select f.id, f.name, f.description, f.release_date, f.duration, MPA.ID, MPA.name  " +
                "from film f JOIN MPA ON f.ID_MPA = MPA.ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genreList = filmGenreStorage.findGenresByFilmId(rs.getLong("id"));

        return new Film(rs.getLong("FILM.id"), new MPA(rs.getLong("MPA.id"), rs.getString("MPA.name")),
                rs.getString("FILM.name"), rs.getString("FILM.description"),
                rs.getDate("FILM.release_date").toLocalDate(), rs.getInt("FILM.duration"), genreList);
    }

    private void updateGenresOfFilm(Long filmId, List<Genre> genres) {
        //удалить старые жанры у фильма
        filmGenreStorage.delete(filmId);

        this.jdbcTemplate.batchUpdate(
                "INSERT INTO film_genre(id_film, id_genre) SELECT ?, ? " +
                        "WHERE NOT EXISTS (SELECT ID_FILM, ID_GENRE FROM film_genre WHERE ID_FILM = ? AND ID_GENRE = ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = genres.get(i);
                        ps.setLong(1, filmId);
                        ps.setLong(2, genre.getId());
                        ps.setLong(3, filmId);
                        ps.setLong(4, genre.getId());
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }
}
