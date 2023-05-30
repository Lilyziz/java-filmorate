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
        if (!userStorage.contains(user.getId())) {
            throw new NotFoundException("There is no user with this id");
        }
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
        if (!userStorage.contains(id)) {
            throw new NotFoundException("There is no user with this id");
        }
        return userStorage.findById(id);
    }

    public void addFriend(long id, long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Id must be positive number");
        }
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            throw new NotFoundException("There is no user with this id");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Id must be positive number");
        }
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            throw new NotFoundException("There is no user with this id");
        }
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        return userStorage.findFriends(id);
    }

    public List<User> getCommonFriends(long id1, long id2) {
        return userStorage.findCommonFriends(id1, id2);
    }
}
