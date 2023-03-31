package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MPA.MPADbStorage;
import ru.yandex.practicum.filmorate.storage.MPA.MPAStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmStorage;
    private MPAStorage mpaStorage;
    private FilmGenreStorage filmGenreStorage;
    private GenreStorage genreStorage;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        mpaStorage = new MPADbStorage(jdbcTemplate);
        genreStorage = new GenreDbStorage(jdbcTemplate);
        filmGenreStorage = new FilmGenreDbStorage(jdbcTemplate, genreStorage);
        filmStorage = new FilmDbStorage(jdbcTemplate, mpaStorage, filmGenreStorage);
        testFilm = new Film("mail@yandex.ru",
                "doloreUpdate2", LocalDate.of(2000, 5, 10), 130);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testGetFilm() {
        Film film = filmStorage.get(1);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testGetAllFilm() {
        List<Film> filmList = filmStorage.getAll();
        assertThat(filmList.size()).isEqualTo(3);
    }

    @Test
    public void testCreateFilm() {
        testFilm.setMpa(new MPA(1L, "G"));
        filmStorage.create(testFilm);
        Film film = filmStorage.get(2);
        assertThat(film).hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    public void testUpdateFilm() {
        Film testNewFilm = new Film(1L, new MPA(1L, "G"), "test film",
                "doloreUpdate2", LocalDate.of(2000, 5, 10), 130);
        filmStorage.update(testNewFilm);
        Film film = filmStorage.get(1);
        assertThat(film).hasFieldOrPropertyWithValue("name", "test film");
    }

    @Test
    public void testDeleteFilm() {
        filmStorage.delete(3L);
        List<Film> filmList = filmStorage.getAll();
        assertThat(filmList.size()).isEqualTo(2);
    }

}
