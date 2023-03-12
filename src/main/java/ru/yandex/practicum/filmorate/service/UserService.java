package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User get(long userId) {
        validateId(userId);
        return userStorage.get(userId);
    }

    public User create(User user) {
        validateWithExceptions(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validateId(user.getId());
        validateWithExceptions(user);
        return userStorage.update(user);
    }

    private void validateWithExceptions(User user) {
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

    private void validateId(Long id) {
        if (userStorage.get(id) == null || id < 0) {
            throw new DataNotFoundException("Пользователь с таким id не найден");
        }
    }


    public void addFriend(long userId, long friendId) {
        validateId(userId);
        validateId(friendId);
        final User user = userStorage.get(userId);
        final User friend = userStorage.get(friendId);
        user.getFriendIds().add(friendId);
        friend.getFriendIds().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        final User user = userStorage.get(userId);
        final User friend = userStorage.get(friendId);
        user.getFriendIds().remove(friendId);
        friend.getFriendIds().remove(userId);
    }

    public List<User> getFriends(long id) {
        ArrayList<User> friendsArrayList = new ArrayList<>();
        for (Long i : userStorage.get(id).getFriendIds()) {
            friendsArrayList.add(userStorage.get(i));
        }
        return friendsArrayList;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        ArrayList<User> commonFriendsArrayList = new ArrayList<>();
        if (userStorage.get(id) == null || userStorage.get(otherId) == null) {
            return commonFriendsArrayList;
        }
        if (userStorage.get(id).getFriendIds() == null || userStorage.get(otherId).getFriendIds() == null) {
            return commonFriendsArrayList;
        }
        for (Long i : userStorage.get(id).getFriendIds()) {
            for (Long j : userStorage.get(otherId).getFriendIds()) {
                if (i == j) {
                    commonFriendsArrayList.add(userStorage.get(i));
                }
            }
        }
        return commonFriendsArrayList;
    }

}
