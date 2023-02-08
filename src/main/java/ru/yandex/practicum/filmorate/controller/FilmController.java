package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


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

    private final HashMap<Integer, Film> films = new HashMap();
    private int filmId = 1;


    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<Film>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if (film.validate()) {
            film.setId(filmId++);
            films.put(film.getId(), film);
            log.info("Добавлен фильм с id = {}", film.getId());
        } else {
            throw new ValidationException("Ошибка валидации фильма");
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        if (film.validate() && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Изменён фильм с id = {}", film.getId());
        } else {
            throw new ValidationException("Ошибка валидации фильма");
        }
        return film;
    }

}
