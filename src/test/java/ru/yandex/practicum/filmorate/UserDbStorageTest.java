package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private UserDbStorage userStorage;
    private User testUser;

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        userStorage = new UserDbStorage(jdbcTemplate);
        testUser = new User("mail@yandex.ru",
                "doloreUpdate2", "est adipisicing2", LocalDate.of(2000, 5, 10));
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testGetUser() {
        User user = userStorage.get(1);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testGetAllUser() {
        List<User> userList = userStorage.getAll();
        assertThat(userList.size()).isEqualTo(4);
    }

    @Test
    public void testCreateUser() {
        userStorage.create(testUser);
        User user = userStorage.get(2);
        assertThat(user).hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    public void testUpdateUser() {
        User testNewUser = new User(1L, "mail@yandex.ru",
                "doloreUpdate2", "est adipisicing2", LocalDate.of(2000, 5, 10));
        userStorage.update(testNewUser);
        User user = userStorage.get(1);
        assertThat(user).hasFieldOrPropertyWithValue("login", "doloreUpdate2");
    }

    @Test
    public void testDeleteUser() {
        userStorage.delete(4L);
        List<User> userList = userStorage.getAll();
        assertThat(userList.size()).isEqualTo(3);
    }

}
