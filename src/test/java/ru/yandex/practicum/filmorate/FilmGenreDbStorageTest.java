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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDbStorageTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private FilmGenreDbStorage filmGenreStorage;
    private GenreDbStorage genreStorage;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        genreStorage = new GenreDbStorage(jdbcTemplate);
        filmGenreStorage = new FilmGenreDbStorage(jdbcTemplate, genreStorage);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void updateGenresOfFilm() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        filmGenreStorage.updateGenresOfFilm(1L, genres);

        List<Genre> newGenres = filmGenreStorage.findGenresByFilmId(1L);
        assertThat(newGenres.size()).isEqualTo(1);

    }
}
