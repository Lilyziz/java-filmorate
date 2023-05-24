package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Get list of all films: ");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("Get film with id {}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilmsWithCount(@RequestParam
           (value = "count",required = false, defaultValue = "10") long count) {
        log.info("Top films:");
        return filmService.topFilmsWithCount(count);
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Create film: {}", film.toString());
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Update film: {}", film.toString());
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void userGiveLikeToFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Add like to film with id {} from user with id {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable long filmId) {
        log.info("Delete film with id {}", filmId);
        filmService.delete(filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void userDeleteLikeToFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete like to film with id {} from user with id {}", id, userId);
        filmService.deleteLike(id, userId);
    }
}
