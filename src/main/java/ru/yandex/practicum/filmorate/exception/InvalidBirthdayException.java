package ru.yandex.practicum.filmorate.exception;

public class InvalidBirthdayException extends RuntimeException {
    public InvalidBirthdayException(String message) {
        super(message);
    }
}
