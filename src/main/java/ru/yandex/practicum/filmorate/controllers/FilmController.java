package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controllers.Exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class FilmController {
    int id = 0;
    private final List<Film> films = new ArrayList<>();

    int generateId() {
        return ++id;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.info("Get all films: " + films.size());
        return films;
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) throws InvalidFilmNameException, InvalidFilmDescriptionException, InvalidFilmDurationException, InvalidFilmReleaseDateException {

        log.info("Create film: " + film.toString());

        if (film.getName() == null || film.getName() == "") {
            throw new InvalidFilmNameException("Name не указан");
        }

        if (film.getDescription().length() > 200) {
            throw new InvalidFilmDescriptionException("Description должно быть до 200 символов");
        }
        if (film.getDuration() <= 0) {
            throw new InvalidFilmDurationException("Duration должна быть неотрицательной");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new InvalidFilmReleaseDateException("ReleaseDate старая");
        }
        film.setId(generateId());
        films.add(film);
        return film;
    }

    @PutMapping("/films")
    public Film put(@RequestBody Film film) throws InvalidFilmException {

        log.info("Update film: " + film.toString());

        if (film.getName() == null || film.getName() == "") {
            throw new InvalidFilmException("Name не указан");
        }

        for (Film item : films) {
            if (item.getId() == film.getId()) {
                item.update(film);
                return item;
            }
        }
        throw new InvalidFilmException("Film не найден");
    }
}
