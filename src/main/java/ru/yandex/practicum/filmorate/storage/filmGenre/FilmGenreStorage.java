package ru.yandex.practicum.filmorate.storage.filmGenre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmGenreStorage {
    void updateGenresOfFilm(Long filmId, List<Genre> genres);

    void delete(long id);

    List<Genre> findGenresByFilmId(Long filmId);
}
