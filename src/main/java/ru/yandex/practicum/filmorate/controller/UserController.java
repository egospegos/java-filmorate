package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
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
        ArrayList<User> userArrayList = new ArrayList<User>(users.values());
        log.info("Количество пользователей в списке = {}", userArrayList.size());
        return userArrayList;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateWithExceptions(user);
        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь с id = {}", user.getId());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateWithExceptions(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь по ключу с id: " + user.getId() + " не найден");
        }
        users.put(user.getId(), user);
        log.info("Изменён пользователь с id = {}", user.getId());
        return user;
    }

    public void validateWithExceptions(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Ошибка валидации пользователя по email");
        }

        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка валидации пользователя по логину");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка валидации пользователя по дню рождения");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }


}
