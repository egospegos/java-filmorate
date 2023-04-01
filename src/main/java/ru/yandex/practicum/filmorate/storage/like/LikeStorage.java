package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    void addLike(long id, long userId);

    void removeLike(long id, long userId);

    Integer countFilmLikes(long filmId);

    List<Film> getPopular(int count);
}
