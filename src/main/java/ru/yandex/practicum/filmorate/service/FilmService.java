package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.IFilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorageDb;
import ru.yandex.practicum.filmorate.storage.user.UserStorageDb;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final IFilmStorage filmStorage;
    private final LikeStorageDb likeStorage;
    private final UserStorageDb userStorage;
    private final FilmValidator filmValidator = new FilmValidator();

    public Film addFilm(Film film) {
        filmValidator.isValid(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        filmValidator.isValid(film);
        if (!filmStorage.contains(film.getId())) {
            throw new NotFoundException("There is no film with this id");
        }
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("There is no film with this id");
        }
        return filmStorage.getFilmById(id);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public void addLike(long id, long userId) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("There is no film with this id");
        }
        if (!userStorage.contains(id)) {
            throw new NotFoundException("There is no user with this id");
        }
        likeStorage.addLike(id, userId);
    }

    public void deleteLike(long filmId, long userId) {
        if (!filmStorage.contains(filmId)) {
            throw new NotFoundException("There is no film with this id");
        }
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("There is no user with this id");
        }
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> topFilmsWithCount(long count) {
        return filmStorage.getTopFilmsWithCount(count);
    }
}
