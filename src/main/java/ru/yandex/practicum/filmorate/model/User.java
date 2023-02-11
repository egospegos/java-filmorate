package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.Duration;
import java.time.LocalDate;


@RequiredArgsConstructor
@Getter
@Setter
public class User {
    private Long id;

    @NonNull
    @NotEmpty (message = "Email should not be empty")
    @Email (message = "Email should be valid")
    private String email;

    @NonNull
    @NotBlank(message = "Login should not be empty")
    private String login;
    private String name;
    @Past(message = "birthday should be in the past")
    private LocalDate birthday;

}
