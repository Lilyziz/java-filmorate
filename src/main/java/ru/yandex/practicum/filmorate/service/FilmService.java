package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.IFilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.Set;

@Service
public class FilmService {
    private final IFilmStorage filmStorage;
    private final FilmValidator filmValidator;

    public FilmService(IFilmStorage filmStorage, UserService userService) {
        filmValidator = new FilmValidator();
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        filmValidator.isValid(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        filmValidator.isValid(film);
        if (!filmStorage.contains(film.getId())) {
            throw new IdException("There is no film with this id");
        }
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) {
        if (!filmStorage.contains(id)) {
            throw new IdException("There is no film with this id");
        }
        return filmStorage.getFilmById(id);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public void addLike(long id, long userId) {
        if (!filmStorage.contains(id)) {
            throw new IdException("There is no film with this id");
        }
        Set<Long> likes = filmStorage.getFilmById(id).getLikes();
        if (!likes.add(userId)) {
            throw new IdException("User already liked this film");
        }
    }

    public void deleteLike(long id, long userId) {
        if (!filmStorage.contains(id)) {
            throw new IdException("There is no film with this id");
        }
        Set<Long> likes = filmStorage.getFilmById(id).getLikes();
        if (!likes.contains(userId)) {
            throw new IdException("User didn't like this film");
        }
        likes.remove(userId);
    }

    public Collection<Film> topFilmsWithCount(long count) {
        return filmStorage.topFilmsWithCount(count);
    }
}
