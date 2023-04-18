package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class InvalidFilmDescriptionException extends Throwable {
    public InvalidFilmDescriptionException(String message) {
        super(message);
    }
}
