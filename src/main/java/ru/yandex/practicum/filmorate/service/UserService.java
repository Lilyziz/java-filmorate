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

    public User addUser(User user) {
        userValidator.isValid(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userValidator.isValid(user);
        if (!userStorage.contains(user.getId())) {
            throw new NotFoundException("There is no user with this id");
        }
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public User getUserById(long id) {
        if (!userStorage.contains(id)) {
            throw new NotFoundException("There is no user with this id");
        }
        return userStorage.getUserById(id);
    }

    public void addFriends(long id, long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Id must be positive number");
        }
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            throw new NotFoundException("There is no user with this id");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFromFriends(long id, long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Id must be positive number");
        }
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            throw new NotFoundException("There is no user with this id");
        }
        userStorage.deleteFromFriends(id, friendId);
    }

    public List<User> getFriendList(long id) {
        return userStorage.getFriendList(id);
    }

    public List<User> getCommonFriends(long id1, long id2) {
        return userStorage.getCommonFriends(id1, id2);
    }
}
