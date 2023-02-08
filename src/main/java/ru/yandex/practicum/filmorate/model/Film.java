package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;

@Data
@Slf4j
public class Film {
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private int duration;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate FIRST_MOVIE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    public boolean validate() {
        if (name.isEmpty()) {
            log.info("Название фильма не может быть пустым");
            return false;
        }

        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            log.info("Максимальная длина описания — 200 символов");
            return false;
        }

        if (releaseDate.isBefore(FIRST_MOVIE_DATE)) {
            log.info("Дата релиза — не раньше 28 декабря 1895 года");
            return false;
        }

        if (duration <= 0) {
            log.info("Продолжительность фильма должна быть положительной");
            return false;
        }
        return true;
    }
}
