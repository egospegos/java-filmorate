package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long id;

    @NonNull
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NonNull
    @NotBlank(message = "Login should not be empty")
    private String login;
    private String name;
    @Past(message = "birthday should be in the past")
    private LocalDate birthday;

    @JsonIgnore
    private Set<Long> friendIds = new HashSet<>();

    public User(Long id, @NonNull String email, @NonNull String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(@NonNull String email, @NonNull String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(@NonNull String email, @NonNull String login) {
        this.email = email;
        this.login = login;
    }

    public User(@NonNull String email, @NonNull String login, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}
