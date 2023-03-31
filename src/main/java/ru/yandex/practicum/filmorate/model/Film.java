package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Film {
    private Long id;
    private MPA mpa;
    @NonNull
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NonNull
    @Size(max = 200, message = "Description should not be over 200 symbols")
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    @Min(value = 1, message = "duration should be positive")
    private int duration;

    private List<Genre> genres = new ArrayList<>();
    @JsonIgnore
    private Set<Long> userIds = new HashSet<>();

    @JsonIgnore
    private long rate = 0;


    public Film(Long id, @NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(Long id, MPA mpa, @NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration) {
        this.id = id;
        this.mpa = mpa;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(Long id, MPA mpa, @NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration, List<Genre> genres) {
        this.id = id;
        this.mpa = mpa;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
    }
}
