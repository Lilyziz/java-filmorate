package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaStorageDb;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorageDb storage;

    public Mpa getMpaById(final long id) {
        return storage.getMpaById(id);
    }

    public List<Mpa> getAllMpas() {
        return storage.getAllMpas();
    }
}