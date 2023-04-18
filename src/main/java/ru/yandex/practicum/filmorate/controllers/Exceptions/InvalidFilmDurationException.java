package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class InvalidFilmDurationException extends Throwable {
    public InvalidFilmDurationException(String message) {
        super(message);
    }
}
