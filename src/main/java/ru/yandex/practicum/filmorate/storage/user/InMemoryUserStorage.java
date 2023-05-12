package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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

        if (user.getEmail() == null || user.getEmail() == "") {
            throw new ValidationException("No email");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email must be with @");
        }
        for (User item : users.values()) {
            if (item.getEmail().equals(user.getEmail())) {
                throw new ValidationException("User with this email already exist");
            }
        }
        if (user.getLogin() == null || user.getLogin() == "") {
            throw new ValidationException("No login");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login must be without spaces");
        }
        if (user.getName() == null || user.getName() == "") {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday can't be in the future");
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {

        if (user.getEmail() == null || user.getEmail() == "") {
            throw new ValidationException("No email");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email must be with @");
        }
        for (Long item : users.keySet()) {
            if (item == user.getId()) {
                update(users.get(item), user);
                return users.get(item);
            }
        }
        throw new RuntimeException("No email");
    }

    private void update(User updatingUser, User user) {
        if (user.getEmail().contains("@")) {
            updatingUser.setEmail(user.getEmail());
        }
        if (!user.getLogin().isEmpty()) {
            updatingUser.setLogin(user.getLogin());
        }
        if (!user.getName().isEmpty()) {
            updatingUser.setName(user.getName());
        }
        if (!user.getBirthday().isAfter(LocalDate.now())) {
            updatingUser.setBirthday(user.getBirthday());
        }
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
