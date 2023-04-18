package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.Exceptions.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.controllers.Exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.controllers.Exceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.controllers.Exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    static int id = 0;
    private final List<User> users = new ArrayList<>();

    int generateId() {
        return ++id;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("Get all users: " + users.size());
        return users;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) throws InvalidEmailException, UserAlreadyExistException, InvalidLoginException, InvalidBirthdayException {

        log.info("Create user: " + user.toString());

        if (user.getEmail() == null || user.getEmail() == "") {
            throw new InvalidEmailException("Email не указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new InvalidEmailException("Email должен содержать символ @");
        }
        for (User item : users) {
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
        users.add(user);
        return user;
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) throws InvalidEmailException {

        log.info("Update user: " + user.toString());

        if (user.getEmail() == null || user.getEmail() == "") {
            throw new InvalidEmailException("Email не указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new InvalidEmailException("Email должен содержать символ @");
        }
        for (User item : users) {
            System.out.println(item.getId());
            if (item.getId() == user.getId()) {
                item.update(user);
                return user;
            }
        }
        throw new InvalidEmailException("Email не найден");
    }
}
