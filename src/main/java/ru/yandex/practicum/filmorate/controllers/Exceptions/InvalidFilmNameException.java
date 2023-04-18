package ru.yandex.practicum.filmorate.controllers.Exceptions;

public class InvalidFilmNameException extends Throwable {
    public InvalidFilmNameException(String message) {
        super(message);
    }
}
