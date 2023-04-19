package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
public class FilmController {
    private static long id = 0;
    private final HashMap<Long, Film> films = new HashMap<>();

    private long generateId() {
        return ++id;
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        log.info("Get all films: " + films.values().size());
        Collection<Film> listOfFilms = films.values();
        return listOfFilms;
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {

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
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film put(@RequestBody Film film) {

        log.info("Update film: " + film.toString());

        if (film.getName() == null || film.getName() == "") {
            throw new InvalidFilmException("Name не указан");
        }

        for (Long item : films.keySet()) {
            if (item == film.getId()) {
                update(films.get(item), film);
                return films.get(item);
            }
        }
        throw new InvalidFilmException("Film не найден");
    }

    public void update(Film updatingFilm, Film film) {
        if (!film.getName().isEmpty()) {
            updatingFilm.setName(film.getName());
        }
        if (!film.getDescription().isEmpty()) {
            updatingFilm.setDescription(film.getDescription());
        }
        if (film.getDuration() > 0) {
            updatingFilm.setDuration(film.getDuration());
        }
        if (!film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            updatingFilm.setReleaseDate(film.getReleaseDate());
        }
    }
}
