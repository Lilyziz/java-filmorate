package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorageDb;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreStorageDb storage;

    public GenreService(GenreStorageDb genreStorage) {
        this.storage = genreStorage;
    }

    public Genre getById(final long id) {
        Optional<Genre> genre = storage.findById(id);
        if (genre.isEmpty()) {
            throw new NotFoundException("There is no user with this id");
        }
        return genre.get();
    }

    public List<Genre> getAll() {
        return storage.findAll();
    }
}
