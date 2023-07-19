package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MPA {
    private Long id;
    private String name;

    public MPA(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MPA(String name) {
        this.name = name;
    }
}
