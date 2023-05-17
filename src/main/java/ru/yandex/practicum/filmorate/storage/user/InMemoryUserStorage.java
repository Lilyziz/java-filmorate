package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements IUserStorage {
    private static long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    private long generateId() {
        return ++id;
    }

    @Override
    public Collection<User> getAllUsers() {
        Collection<User> listOfUsers = users.values();
        return listOfUsers;
    }

    @Override
    public User createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            update(users.get(user.getId()), user);
            return users.get(user.getId());
        }
        throw new IdException("There is no user with this id");
    }

    private void update(User updatingUser, User user) {
        updatingUser.setEmail(user.getEmail());
        updatingUser.setLogin(user.getLogin());
        updatingUser.setName(user.getName());
        updatingUser.setBirthday(user.getBirthday());
        updatingUser.setFriends(user.getFriends());
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public boolean contains(long id) {
        return users.containsKey(id);
    }
}
