package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreStorageDb {
    private final JdbcTemplate jdbcTemplate;

    public Genre getGenreById(long id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, id);
        if (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("genre_id"));
            genre.setName(genreRows.getString("name"));
            return genre;
        } else {
            throw new NotFoundException("There is no genre with this id");
        }
    }

    public List<Genre> getAllGenres() {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        List<Genre> genresList = new ArrayList<>();
        while (genresRows.next()) {
            Genre genre = new Genre();
            genre.setId(genresRows.getInt("genre_id"));
            genre.setName(genresRows.getString("name"));
            genresList.add(genre);
        }
        return genresList;
    }
}
