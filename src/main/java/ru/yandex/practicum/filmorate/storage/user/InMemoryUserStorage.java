package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements IUserStorage {
    private static long id = 0;
    private final Map<Long, User> users = new HashMap<>();

    private long generateId() {
        return ++id;
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

    @Override
    public User createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.containsKey(user.getId());
        update(users.get(user.getId()), user);
        return users.get(user.getId());
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
