package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final HashMap<Long, User> users = new HashMap();
    private long userId = 1;

    @GetMapping
    public List<User> findAll() {
        log.info("Количество пользователей в списке = {}", users.size());
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь с id = {}", user.getId());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Изменён пользователь с id = {}", user.getId());
        } else {
            throw new ValidationException("Такого пользователя нет");
        }
        return user;
    }

    public static boolean validate(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.info("Электронная почта пустая или не содержит @");
            return false;
        }

        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.info("Логин пустой или содержит пробелы");
            return false;
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения не может быть в будущем");
            return false;
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return true;
    }

}
