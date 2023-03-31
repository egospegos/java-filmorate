package ru.yandex.practicum.filmorate.storage.friendship;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<Long> getFriendIds(long userId);
}
