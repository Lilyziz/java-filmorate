package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.IUserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserStorage userStorage;
    private final UserValidator userValidator = new UserValidator();

    public User create(User user) {
        userValidator.isValid(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        userValidator.isValid(user);
        userStorage.contains(user.getId()).orElseThrow(
                () -> new NotFoundException("There is no user with this id:" + user.getId()));
        userStorage.update(user);
        return user;
    }

    public List<User> getAll() {
        return userStorage.findAll();
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public User getById(long id) {
        return userStorage.findById(id).orElseThrow(
                () -> new NotFoundException("There is no user with this id: " + id));
    }

    public void addFriend(long id, long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Id must be positive number");
        }
        userStorage.findById(id).orElseThrow(
                () -> new NotFoundException("There is no user with this id: " + id));
        userStorage.findById(friendId).orElseThrow(
                () -> new NotFoundException("There is no user with this id: " + id));
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Id must be positive number");
        }
        userStorage.findById(id).orElseThrow(
                () -> new NotFoundException("There is no user with this id: " + id));
        userStorage.findById(friendId).orElseThrow(
                () -> new NotFoundException("There is no user with this id: " + id));
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        return userStorage.findFriends(id);
    }

    public List<User> getCommonFriends(long id1, long id2) {
        return userStorage.findCommonFriends(id1, id2);
    }
}
