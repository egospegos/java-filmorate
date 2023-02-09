package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;

@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
public class Film {
    private Long id;
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



}
