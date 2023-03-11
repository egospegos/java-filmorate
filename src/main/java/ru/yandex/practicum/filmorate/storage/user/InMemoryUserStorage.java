package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap();
    private long userId = 1;

    @Override
    public List<User> getAll() {
        ArrayList<User> userArrayList = new ArrayList<User>(users.values());
        log.info("Количество пользователей в списке = {}", userArrayList.size());
        return userArrayList;
    }

    @Override
    public User get(long userId){
        return users.get(userId);
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
    }

    @Override
    public User create(@Valid @RequestBody User user) {
        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь с id = {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь по ключу с id: " + user.getId() + " не найден");
        }
        users.put(user.getId(), user);
        log.info("Изменён пользователь с id = {}", user.getId());
        return user;
    }


}
