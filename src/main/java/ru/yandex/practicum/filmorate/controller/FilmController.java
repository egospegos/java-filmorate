package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final HashMap<Long, Film> films = new HashMap();
    private long filmId = 1;

    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FIRST_MOVIE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    @GetMapping
    public List<Film> findAll() {
        log.info("Количество фильмов в списке = {}", films.size());
        return new ArrayList<Film>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (validate(film)) {
            film.setId(filmId++);
            films.put(film.getId(), film);
            log.info("Добавлен фильм с id = {}", film.getId());
        } else {
            throw new ValidationException("Ошибка валидации фильма");
        }
        return film;

        // как составить конструкцию try catch, если метод должен возвращать Film.
        // А при плохой валидации я не должен ничего возвращать
        /*
        try {
            if (validate(film)) {
                film.setId(filmId++);
                films.put(film.getId(), film);
                log.info("Добавлен фильм с id = {}", film.getId());
            }
            return film;

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

         */

    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(FIRST_MOVIE_DATE)) {
            throw new ValidationException("Ошибка валидации фильма по дате релиза");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Изменён фильм с id = {}", film.getId());
        } else {
            throw new ValidationException("Ошибка валидации фильма");
        }
        return film;
    }

    public static boolean validateWithExceptions(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.info("Название фильма не может быть пустым");
            throw new ValidationException("Ошибка валидации фильма по названию");
        }

        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.info("Максимальная длина описания — 200 символов");
            throw new ValidationException("Ошибка валидации фильма по описанию");
        }

        if (film.getReleaseDate().isBefore(FIRST_MOVIE_DATE)) {
            log.info("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Ошибка валидации фильма по дате релиза");
        }

        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Ошибка валидации фильма по продолжительности");
        }
        return true;
    }

    public static boolean validate(Film film) {
        if (film.getName().isEmpty()) {
            log.info("Название фильма не может быть пустым");
            return false;
        }

        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.info("Максимальная длина описания — 200 символов");
            return false;
        }

        if (film.getReleaseDate().isBefore(FIRST_MOVIE_DATE)) {
            log.info("Дата релиза — не раньше 28 декабря 1895 года");
            return false;
        }

        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма должна быть положительной");
            return false;
        }
        return true;
    }

}
