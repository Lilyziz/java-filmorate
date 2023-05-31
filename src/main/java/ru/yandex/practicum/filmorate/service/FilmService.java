package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmGenreStorageDb;
import ru.yandex.practicum.filmorate.storage.film.GenreStorageDb;
import ru.yandex.practicum.filmorate.storage.film.IFilmStorage;
import ru.yandex.practicum.filmorate.storage.film.LikeStorageDb;
import ru.yandex.practicum.filmorate.storage.user.UserStorageDb;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final IFilmStorage filmStorage;
    private final LikeStorageDb likeStorage;
    private final UserStorageDb userStorage;
    private final GenreStorageDb genreStorage;
    private final FilmGenreStorageDb filmGenreStorage;
    private final FilmValidator filmValidator = new FilmValidator();

    public Film create(Film film) {
        filmValidator.isValid(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        filmValidator.isValid(film);
        if (!filmStorage.contains(film.getId())) {
            throw new NotFoundException("There is no film with this id");
        }
        updateGenresForFilmId(film.getId(), film.getGenres());
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        List<Film> filmsList = filmStorage.findAll();
        //List<Genre> genreList = genreStorage.findAll();
//
        //for (Film film : filmsList) {
        //    Set<Genre> filmsGenre = new TreeSet<>();
        //    for (Genre genre : film.getGenres()) {
        //        filmsGenre.add(genreList.get(genre.getId()));
        //    }
        //    film.setGenres(filmsGenre);
        //    System.out.println(filmsGenre);
        //}
        Map<Integer, Set<Genre>> filmGenresMap = filmGenreStorage.getAllFilmGenres(filmsList);
        for (Film film : filmsList) {
            film.setGenres(filmGenresMap.get(film.getId()));
        }
        return filmsList;
    }

    public Film getById(long id) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("There is no film with this id");
        }
        Film film = filmStorage.findById(id);
        film.setGenres(filmGenreStorage.findAllFilmGenresById(id));
        return film;
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

    public List<Film> getTopFilmsWithCount(long count) {
        return filmStorage.findTopFilmsWithCount(count);
    }

    public void updateGenresForFilmId(long filmId, Set<Genre> genres) {
        filmGenreStorage.deleteAll(filmId);
        filmGenreStorage.add(filmId, genres);
    }
}
