package ru.yandex.practicum.filmorate.storage.MPA;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MPADbStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA get(long id) {
        String sqlQuery = "select id, name " +
                "from mpa where id = ?";
        try {
            MPA mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException("Mpa с таким id не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
    }

    @Override
    public List<MPA> getAll() {
        String sqlQuery = "select id, name from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public MPA findMpaById(long id) {
        String sqlQuery = "select id, name " +
                "from mpa where id = ?";
        try {
            MPA mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException("Mpa с таким id не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
    }

    private MPA mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(rs.getLong("id"), rs.getString("name"));
    }
}
