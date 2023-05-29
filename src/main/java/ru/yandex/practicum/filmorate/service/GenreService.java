package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorageDb;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorageDb storage;

    public GenreService(GenreStorageDb genreStorage) {
        this.storage = genreStorage;
    }

    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }

    public Genre getGenreById(final long id) {
        return storage.getGenreById(id);
    }
}
