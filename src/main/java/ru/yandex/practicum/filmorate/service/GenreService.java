package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorageDb;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorageDb storage;

    public Genre getById(final long id) {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no genre with this id: " + id));
    }

    public List<Genre> getAll() {
        return storage.findAll();
    }
}
