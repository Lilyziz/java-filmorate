package ru.yandex.practicum.filmorate.exception;

public class InvalidFilmDescriptionException extends RuntimeException {
    public InvalidFilmDescriptionException(String message) {
        super(message);
    }
}
