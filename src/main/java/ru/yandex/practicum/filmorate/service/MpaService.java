package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorageDb;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorageDb storage;

    public Mpa getById(final long id) {
        Optional<Mpa> mpa = storage.findById(id);
        if (mpa.isEmpty()) {
            throw new NotFoundException("There is no user with this id");
        }
        return mpa.get();
    }

    public List<Mpa> getAll() {
        return storage.findAll();
    }
}