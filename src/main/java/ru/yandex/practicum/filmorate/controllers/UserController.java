package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exception.InvalidEmailException;
import ru.yandex.practicum.filmorate.exception.InvalidLoginException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
public class UserController {

    private static long id = 0;
    private final HashMap<Long, User> users = new HashMap<>();

    private long generateId() {
        return ++id;
    }

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        log.info("Get all users: " + users.values().size());
        Collection<User> listOfUsers = users.values();
        return listOfUsers;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {

        log.info("Create user: " + user.toString());

        if (user.getEmail() == null || user.getEmail() == "") {
            throw new InvalidEmailException("Email не указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new InvalidEmailException("Email должен содержать символ @");
        }
        for (User item : users.values()) {
            if (item.getEmail().equals(user.getEmail())) {
                throw new UserAlreadyExistException("Пользователь с таким email уже существует");
            }
        }
        if (user.getLogin() == null || user.getLogin() == "") {
            throw new InvalidLoginException("Login не указан");
        }
        if (user.getLogin().contains(" ")) {
            throw new InvalidLoginException("Login должен быть без пробелов");
        }
        if (user.getName() == null || user.getName() == "") {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidBirthdayException("Birthday не может быть в будущем");
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) {

        log.info("Update user: " + user.toString());

        if (user.getEmail() == null || user.getEmail() == "") {
            throw new InvalidEmailException("Email не указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new InvalidEmailException("Email должен содержать символ @");
        }
        for (Long item : users.keySet()) {
            if (item == user.getId()) {
                update(users.get(item), user);
                return users.get(item);
            }
        }
        throw new InvalidEmailException("Email не найден");
    }

    public void update(User updatingUser, User user) {
        if (user.getEmail().contains("@")) {
            updatingUser.setEmail(user.getEmail());
        }
        if (!user.getLogin().isEmpty()) {
            updatingUser.setLogin(user.getLogin());
        }
        if (!user.getName().isEmpty()) {
            updatingUser.setName(user.getName());
        }
        if (!user.getBirthday().isAfter(LocalDate.now())) {
            updatingUser.setBirthday(user.getBirthday());
        }
    }
}
