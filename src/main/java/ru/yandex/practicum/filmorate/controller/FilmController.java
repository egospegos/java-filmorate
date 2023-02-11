package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


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
        ArrayList<Film> filmArrayList = new ArrayList<Film>(films.values());
        log.info("Количество фильмов в списке = {}", filmArrayList.size());
        return filmArrayList;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateWithExceptions(film);
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм с id = {}", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateWithExceptions(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм по ключу с id: " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Изменён фильм с id = {}", film.getId());
        return film;
    }

    public void validateWithExceptions(Film film) {
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

}
