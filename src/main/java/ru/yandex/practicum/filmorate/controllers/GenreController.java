package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Get list of all genres: ");
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable long id) {
        log.info("Get genre with id {}", id);
        return genreService.getById(id);
    }
}
