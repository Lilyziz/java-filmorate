package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public void update(Film film) {
        if (!film.getName().isEmpty()) {
            this.name = film.getName();
        }
        if (!film.getDescription().isEmpty()) {
            this.description = film.getDescription();
        }
        if (film.getDuration() > 0) {
            this.duration = film.getDuration();
        }
        if (!film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            this.releaseDate = film.getReleaseDate();
        }
    }
}
