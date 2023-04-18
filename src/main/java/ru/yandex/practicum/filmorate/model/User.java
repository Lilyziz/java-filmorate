package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public void update(User user) {
        if (!user.getEmail().contains("@")) {
            this.email = user.getEmail();
        }
        if (!user.getLogin().isEmpty()) {
            this.login = user.getLogin();
        }
        if (!user.getName().isEmpty()) {
            this.name = user.getName();
        }
        if (!user.getBirthday().isAfter(LocalDate.now())) {
            this.birthday = user.getBirthday();
        }
    }
}
