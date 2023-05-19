package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.IUserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Service
public class UserService {
    private final IUserStorage userStorage;
    private final UserValidator userValidator;

    public UserService(IUserStorage userStorage) {
        userValidator = new UserValidator();
        this.userStorage = userStorage;
    }

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

    public void addFriend(long id, long friendId) {
        if (id < 1 || friendId < 1) {
            throw new NotFoundException("Id must be positive number");
        }

        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            throw new NotFoundException("There is no user with this id");
        }
        User user1 = getUserById(id);
        User user2 = getUserById(friendId);

        if (!user1.getFriends().add(friendId)) {
            throw new NotFoundException("Users are already friends");
        }
        if (!user2.getFriends().add(id)) {
            throw new NotFoundException("Users are already friends");
        }
    }

    public void deleteFromFriends(long id, long friendId) {
        User user1 = getUserById(id);
        User user2 = getUserById(friendId);

        if (!user1.getFriends().remove(friendId)) {
            throw new NotFoundException("Users are not friends");
        }
        if (!user2.getFriends().remove(id)) {
            throw new NotFoundException("Users are not friends");
        }
    }

    public List<User> getFriendList(long id) {
        Set<Long> friendsIds = userStorage.getUserById(id).getFriends();
        if (friendsIds == null || friendsIds.size() == 0) {
            throw new NotFoundException("List of friends is empty");
        }
        List<User> listOfFriends = new ArrayList<>();
        for (Long friendsId : friendsIds) {
            listOfFriends.add(userStorage.getUserById(friendsId));
        }
        return listOfFriends;
    }

    public List<User> getCommonFriends(long id1, long id2) {
        Set<Long> friendsIds1 = userStorage.getUserById(id1).getFriends();
        Set<Long> friendsIds2 = userStorage.getUserById(id2).getFriends();
        Set<Long> commonFriendsIds = new HashSet<>(friendsIds1);
        commonFriendsIds.retainAll(friendsIds2);

        List<User> listOfFriends = new ArrayList<>();
        for (Long friendsId : commonFriendsIds) {
            listOfFriends.add(userStorage.getUserById(friendsId));
        }
        return listOfFriends;
    }
}
