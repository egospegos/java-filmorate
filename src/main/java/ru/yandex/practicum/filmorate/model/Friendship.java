package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friendship {
    private Long id;
    private Long userId;
    private Long friendId;

    public Friendship(Long id, Long userId, Long friendId) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
    }

    public Friendship(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
