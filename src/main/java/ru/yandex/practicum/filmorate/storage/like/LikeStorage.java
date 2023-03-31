package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {
    void addLike(long id, long userId);

    void removeLike(long id, long userId);

    Integer countFilmLikes(long filmId);
}
