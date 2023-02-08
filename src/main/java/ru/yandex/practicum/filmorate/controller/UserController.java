package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap();
    private int userId = 1;

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if (user.validate()) {
            user.setId(userId++);
            users.put(user.getId(), user);
            log.info("Добавлен пользователь с id = {}", user.getId());
        } else {
            throw new ValidationException("Ошибка валидации пользователя");
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        if (user.validate() && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Изменён пользователь с id = {}", user.getId());
        } else {
            throw new ValidationException("Ошибка валидации пользователя");
        }
        return user;
    }

}
