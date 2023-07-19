package ru.yandex.practicum.filmorate.storage.filmGenre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {

    void delete(long id);

    List<Genre> findGenresByFilmId(Long filmId);
}
