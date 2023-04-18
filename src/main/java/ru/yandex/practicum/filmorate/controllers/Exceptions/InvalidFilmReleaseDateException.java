package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class InvalidFilmReleaseDateException extends Throwable {
    public InvalidFilmReleaseDateException(String message) {
        super(message);
    }
}
