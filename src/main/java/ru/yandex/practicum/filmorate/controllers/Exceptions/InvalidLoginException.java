package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class InvalidLoginException extends Throwable {
    public InvalidLoginException(String message) {
        super(message);
    }
}
