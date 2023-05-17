package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdException;
import ru.yandex.practicum.filmorate.model.Film;

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
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            update(films.get(film.getId()), film);
            return films.get(film.getId());
        }
        throw new IdException("Film not found");
    }

    public void update(Film updatingFilm, Film film) {
        updatingFilm.setName(film.getName());
        updatingFilm.setDescription(film.getDescription());
        updatingFilm.setDuration(film.getDuration());
        updatingFilm.setReleaseDate(film.getReleaseDate());
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
