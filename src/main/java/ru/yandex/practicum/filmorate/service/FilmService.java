package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.IFilmStorage;

import java.util.Collection;
import java.util.Set;

@Service
public class FilmService {
    private final IFilmStorage filmStorage;
    private final UserService userService;
    private static long id = 0;

    public FilmService(IFilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    private long generateId() {
        return ++id;
    }

    public Film addFilm(Film film) {
        film.setId(generateId());
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
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

    public String addLike(long id, long userId) {
        if (!filmStorage.contains(id)) {
            throw new IdException("There is no film with this id");
        }
        Set<Long> likes = filmStorage.getFilmById(id).getLikes();
        if (!likes.add(userId)) {
            throw new IdException("User already liked this film");
        }
        return ("User " + userService.getUserById(userId).getLogin() + " поставил лайк фильму "
                + filmStorage.getFilmById(id).getName() + ".");
    }

    public String deleteLike(long id, long userId) {
        if (!filmStorage.contains(id)) {
            throw new IdException("There is no film with this id");
        }
        Set<Long> likes = filmStorage.getFilmById(id).getLikes();
        if (!likes.contains(userId)) {
            throw new IdException("User didn't like this film");
        }
        likes.remove(userId);
        return ("User " + userService.getUserById(userId).getLogin() + " delete like from film "
                + filmStorage.getFilmById(id).getName() + ".");
    }

    public Collection<Film> topFilmsWithCount(long count) {
        return filmStorage.topFilmsWithCount(count);
    }
}
