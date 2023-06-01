package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserStorage {
    List<User> findAll();

    User create(User user);

    void update(User user);

    void delete(Long id);

    Optional<User> findById(Long id);

    Optional<Boolean> contains(long id);

    void addFriend(long user1, long friend2);

    List<User> findFriends(Long id);

    void deleteFriend(long userId, long friendId);

    List<User> findCommonFriends(long userId, long friendId);
}