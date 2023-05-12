package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements IFilmStorage {
    private static long id = 0;
    private final Map<Long, Film> films = new HashMap<>();

    private long generateId() {
        return ++id;
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> listOfFilms = films.values();
        return listOfFilms;
    }

    @Override
    public Film createFilm(Film film) {

        if (film.getName() == null || film.getName() == "") {
            throw new ValidationException("No name");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Description must be >200");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Duration must be >0");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("ReleaseDate too old");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        if (film.getName() == null || film.getName() == "") {
            throw new ValidationException("No name");
        }

        for (Long item : films.keySet()) {
            if (item == film.getId()) {
                update(films.get(item), film);
                return films.get(item);
            }
        }
        throw new IdException("Film not found");
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

    @Override
    public Film getFilmById(long id) {
        return films.get(id);
    }

    @Override
    public boolean contains(long id) {
        return films.containsKey(id);
    }

    @Override
    public void delete(long id) {
        films.remove(id);
    }

    @Override
    public Collection<Film> topFilmsWithCount(long count) {
        Collection<Film> allFilms = getAllFilms().stream()
                .sorted(Comparator.comparing(film -> -film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
        return allFilms;
    }
}
