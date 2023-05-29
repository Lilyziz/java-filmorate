package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface IUserStorage {
    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void delete(Long id);

    User getUserById(Long id);

    boolean contains(long id);

    void addFriend(long user1, long friend2);

    List<User> getFriendList(Long id);

    void deleteFromFriends(long userId, long friendId);

    List<User> getCommonFriends(long userId, long friendId);
}