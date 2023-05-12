package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface IUserStorage {
    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    //void update(User updatingUser, User user);
    void delete(Long id);

    User getUserById(Long id);
    
    boolean contains(long id);
}
