package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FIRST_MOVIE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film get(long filmId) {
        validateId(filmId);
        return filmStorage.get(filmId);
    }

    public void delete(long filmId) {
        validateId(filmId);
        filmStorage.delete(filmId);
    }

    public Film create(Film film) {
        validateWithExceptions(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateId(film.getId());
        validateWithExceptions(film);
        return filmStorage.update(film);
    }

    private void validateWithExceptions(Film film) {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Ошибка валидации фильма по названию");
        }

        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Ошибка валидации фильма по описанию");
        }

        if (film.getReleaseDate().isBefore(FIRST_MOVIE_DATE)) {
            throw new ValidationException("Ошибка валидации фильма по дате релиза");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Ошибка валидации фильма по продолжительности");
        }
    }

    private void validateId(Long id) {
        if (filmStorage.get(id) == null || id < 0) {
            throw new DataNotFoundException("Фильм с таким id не найден");
        }
    }

    public void addLike(long id, long userId) {
        likeStorage.addLike(id, userId);
    }

    public void removeLike(long id, long userId) {
        validateId(id);
        validateId(userId);
        likeStorage.removeLike(id, userId);
    }

    public List<Film> getPopular(int count) {
        List<Film> films = likeStorage.getPopular(count);
        //если лайков нет, то вывести все фильмы
        if (films.size() == 0) films = filmStorage.getAll();
        return films;

    }

    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingLong(Film::getRate).reversed();
}
