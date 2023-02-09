package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void userValidate() {
        LocalDate birthday = LocalDate.of(1998, Month.MAY, 29);
        User user = new User("bel@mail.ru", "Егос");
        user.setName("Егор");
        user.setBirthday(birthday);
        assertTrue(UserController.validate(user));
    }

    @Test
    public void userValidateWithIncorrectMail() {
        LocalDate birthday = LocalDate.of(1998, Month.MAY, 29);
        User user = new User("", "Егос");
        user.setName("Егор");
        user.setBirthday(birthday);
        assertFalse(UserController.validate(user));
    }

    @Test
    public void userValidateWithIncorrectLogin() {
        LocalDate birthday = LocalDate.of(1998, Month.MAY, 29);
        User user = new User("gmail@gmail", "Егос ");
        user.setName("Егор");
        user.setBirthday(birthday);
        assertFalse(UserController.validate(user));
    }

    @Test
    public void userValidateWithIncorrectBirthday() {
        LocalDate birthday = LocalDate.of(2298, Month.MAY, 29);
        User user = new User("gmail@gmail", "Егос");
        user.setName("Егор");
        user.setBirthday(birthday);
        assertFalse(UserController.validate(user));
    }

    @Test
    public void userValidateWithEmptyName() {
        LocalDate birthday = LocalDate.of(1998, Month.MAY, 29);
        User user = new User("gmail@gmail", "Егос");
        user.setName("");
        user.setBirthday(birthday);
        UserController.validate(user);
        assertEquals(user.getLogin(), user.getName());
    }

}