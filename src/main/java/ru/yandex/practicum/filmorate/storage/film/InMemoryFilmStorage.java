package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap();
    private long filmId = 1;

    @Override
    public List<Film> getAll() {
        ArrayList<Film> filmArrayList = new ArrayList<Film>(films.values());
        log.info("Количество фильмов в списке = {}", filmArrayList.size());
        return filmArrayList;
    }

    @Override
    public Film get(long filmId){
        return films.get(filmId);
    }

    @Override
    public void delete(long filmId) {
        films.remove(filmId);
    }

    @Override
    public Film create(Film film) {
        film.setId(filmId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм с id = {}", film.getId());
        return film;
    }


    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм по ключу с id: " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Изменён фильм с id = {}", film.getId());
        return film;
    }


}
