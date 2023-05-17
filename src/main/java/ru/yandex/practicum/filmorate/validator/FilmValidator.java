package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    public boolean isValid(Film film) throws ValidationException {
        nameValidator(film.getName());
        descriptionValidator(film.getDescription());
        releaseDateValidator(film.getReleaseDate());
        durationValidator(film.getDuration());
        return true;
    }

    private boolean nameValidator(String name) throws ValidationException {
        if (name == null || name == "") {
            throw new ValidationException("No name");
        }
        return true;
    }

    private boolean descriptionValidator(String description) throws ValidationException {
        if (description.length() > 200) {
            throw new ValidationException("Description must be >200");
        }
        return true;
    }

    private boolean releaseDateValidator(LocalDate releaseDate) throws ValidationException {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("ReleaseDate too old");
        }
        return true;
    }

    private boolean durationValidator(int duration) throws ValidationException {
        if (duration <= 0) {
            throw new ValidationException("Duration must be >0");
        }
        return true;
    }
}
