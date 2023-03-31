package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
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
        friendshipStorage.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        friendshipStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(long id) {
        ArrayList<User> friendsArrayList = new ArrayList<>();
        for (Long i : friendshipStorage.getFriendIds(id)) {
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
        for (Long i : friendshipStorage.getFriendIds(id)) {
            for (Long j : friendshipStorage.getFriendIds(otherId)) {
                if (i == j) {
                    commonFriendsArrayList.add(userStorage.get(i));
                }
            }
        }
        return commonFriendsArrayList;
    }

}
