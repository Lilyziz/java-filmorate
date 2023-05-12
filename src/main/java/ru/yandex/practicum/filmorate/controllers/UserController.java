package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("List of all users: ");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        log.info("Get user with id " + id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsList(@PathVariable long id) {
        log.info("List of friends: ");
        return userService.getFriendList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriend(@PathVariable long id, @PathVariable long otherId) {
        log.info("List of common friends: ");
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Create user: " + user.toString());
        return userService.addUser(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.info("Update user: " + user.toString());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
        return ("Users " + userService.getUserById(id).getLogin() + " and "
                + userService.getUserById(friendId).getLogin() + " are friends now");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFromFriends(id, friendId);
        return ("Users " + userService.getUserById(id).getLogin() + " and "
                + userService.getUserById(friendId).getLogin() + " are not friends anymore");

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Delete user with id " + userId);
        userService.delete(userId);
    }
}
