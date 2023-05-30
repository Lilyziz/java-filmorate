package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface IFilmStorage {
    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film findById(long id);

    boolean contains(long id);

    void delete(long id);

    List<Film> findTopFilmsWithCount(long count);
}
