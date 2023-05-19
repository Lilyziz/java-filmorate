package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    public boolean isValid(Film film) throws BadRequestException {
        throwIfNameInvalid(film.getName());
        throwIfDescriptionInvalid(film.getDescription());
        throwIfDataInvalid(film.getReleaseDate());
        throwIfDurationInvalid(film.getDuration());
        return true;
    }

    private void throwIfNameInvalid(String name) throws BadRequestException {
        if (name == null || name == "") {
            throw new BadRequestException("No name");
        }
    }

    private void throwIfDescriptionInvalid(String description) throws BadRequestException {
        if (description.length() > 200) {
            throw new BadRequestException("Description must be >200");
        }
    }

    private void throwIfDataInvalid(LocalDate releaseDate) throws BadRequestException {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new BadRequestException("ReleaseDate too old");
        }
    }

    private void throwIfDurationInvalid(int duration) throws BadRequestException {
        if (duration <= 0) {
            throw new BadRequestException("Duration must be >0");
        }
    }
}
