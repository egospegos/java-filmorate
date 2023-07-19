package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Like {
    private Long id;
    private Long filmId;
    private Long userId;

    public Like(Long id, Long filmId, Long userId) {
        this.id = id;
        this.filmId = filmId;
        this.userId = userId;
    }

    public Like(Long filmId, Long userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}
