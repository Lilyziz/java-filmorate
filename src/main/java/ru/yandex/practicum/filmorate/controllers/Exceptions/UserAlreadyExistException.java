package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
