package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface IFilmStorage {
    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long id);

    boolean contains(long id);

    void delete(long id);

    List<Film> getTopFilmsWithCount(long count);
}
