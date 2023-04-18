package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class InvalidFilmException extends Throwable {
    public InvalidFilmException(String message) {
        super(message);
    }
}
