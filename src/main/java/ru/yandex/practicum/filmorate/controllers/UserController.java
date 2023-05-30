package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        log.info("List of all users: ");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        log.info("Get user with id {}", id);
        return userService.getById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable long id) {
        log.info("List of friends: ");
        return userService.getFriends(id);
    }

    @GetMapping("/{id1}/friends/common/{id2}")
    public List<User> getCommonFriends(@PathVariable long id1, @PathVariable long id2) {
        log.info("List of common friends: ");
        return userService.getCommonFriends(id1, id2);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Create user: {}", user.toString());
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Update user: {}", user.toString());
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
        log.info("Users {} and {} are friends now",
                userService.getById(id).getLogin(),
                userService.getById(friendId).getLogin());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Users {} and {} are not friends anymore",
                userService.getById(id).getLogin(),
                userService.getById(friendId).getLogin());
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Delete user with id {}", userId);
        userService.delete(userId);
    }
}
