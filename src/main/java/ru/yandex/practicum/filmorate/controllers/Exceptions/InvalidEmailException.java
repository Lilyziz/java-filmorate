package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class InvalidEmailException extends Throwable {
    public InvalidEmailException(String message) {
        super(message);
    }
}
