package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorageDb;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorageDb storage;

    public Mpa getById(final long id) {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException("There is no mpa with this id: " + id));
    }

    public List<Mpa> getAll() {
        return storage.findAll();
    }
}