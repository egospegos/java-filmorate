package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import java.time.Month;


public class FilmTest {
/*
    @Test
    public void filmValidate() {
        LocalDate localDate = LocalDate.of(1995, Month.MAY, 28);
        Film film = new Film("Имя", "Описание", localDate, 100);
        FilmController filmController = new FilmController();
        filmController.validateWithExceptions(film);

    }

    @Test
    public void filmValidateWithEmptyName() {
        LocalDate localDate = LocalDate.of(1995, Month.MAY, 28);
        Film film = new Film("", "Описание", localDate, 100);
        FilmController filmController = new FilmController();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.validateWithExceptions(film);
        });

        assertEquals("Ошибка валидации фильма по названию", exception.getMessage());
    }


    @Test
    public void filmValidateWithLongDescription() {
        LocalDate localDate = LocalDate.of(1995, Month.MAY, 28);
        String description = "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. " +
                "о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.";
        Film film = new Film("Имя", description, localDate, 100);
        FilmController filmController = new FilmController();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.validateWithExceptions(film);
        });

        assertEquals("Ошибка валидации фильма по описанию", exception.getMessage());
    }

    @Test
    public void filmValidateWithEarlyReleaseDate() {
        LocalDate localDate = LocalDate.of(1855, Month.MAY, 10);
        Film film = new Film("Имя", "Описание", localDate, 100);
        FilmController filmController = new FilmController();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.validateWithExceptions(film);
        });

        assertEquals("Ошибка валидации фильма по дате релиза", exception.getMessage());
    }

    @Test
    public void filmValidateWithIncorrectDuration() {
        LocalDate localDate = LocalDate.of(1995, Month.MAY, 10);
        Film film = new Film("Имя", "Описание", localDate, -100);
        FilmController filmController = new FilmController();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.validateWithExceptions(film);
        });

        assertEquals("Ошибка валидации фильма по продолжительности", exception.getMessage());
    }



 */
}
