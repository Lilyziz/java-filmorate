package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface IFilmStorage {
    List<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Optional<Film> findById(long id);

    Optional<Boolean> contains(long id);

    void delete(long id);

    List<Film> findTopFilmsWithCount(long count);
}
