package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;

@Slf4j
@Data
public class User {
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    private LocalDate birthday;

    public boolean validate() {
        if (email.isEmpty() || !email.contains("@")) {
            log.info("Электронная почта пустая или не содержит @");
            return false;
        }

        if (login.isEmpty() || login.contains(" ")) {
            log.info("Логин пустой или содержит пробелы");
            return false;
        }

        if (birthday.isAfter(LocalDate.now())) {
            log.info("Дата рождения не может быть в будущем");
            return false;
        }

        if (name == null || name.isEmpty()) {
            name = login;
        }
        return true;
    }
}
