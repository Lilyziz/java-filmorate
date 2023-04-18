package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class InvalidBirthdayException extends Throwable {
    public InvalidBirthdayException(String message) {
        super(message);
    }
}
