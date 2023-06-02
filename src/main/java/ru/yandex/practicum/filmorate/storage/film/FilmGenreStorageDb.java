package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmGenreStorageDb {
    private final JdbcTemplate jdbcTemplate;

    public void deleteAll(long filmId) {
        String sql = "DELETE FROM genre WHERE film_id=?";
        jdbcTemplate.update(sql, filmId);
    }

    public void add(long filmId, Set<Genre> genres) {
        String sql = "INSERT INTO genre (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    public Set<Genre> findAllFilmGenresById(Long filmId) {
        String sql = "SELECT g.genre_id, name FROM genre fg LEFT JOIN genres g ON " +
                "fg.genre_id = g.genre_id WHERE film_id = ?";
        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(sql, filmId);
        Set<Genre> genres = new TreeSet<>();
        while (genresRowSet.next()) {
            Genre genre = new Genre();
            genre.setId(genresRowSet.getInt("genre_id"));
            genre.setName(genresRowSet.getString("name"));
            genres.add(genre);
        }
        return genres;
    }

    public Map<Long, Set<Genre>> getAllFilmGenres(List<Film> films) {
        Map<Long, Set<Genre>> filmGenresMap = new HashMap<>();
        Collection<String> ids = films.stream().map(film -> String.valueOf(film.getId())).collect(Collectors.toList());

        StringJoiner joiner = new StringJoiner(", ");
        for (String s : ids)
            joiner.add(s);
        String result = joiner.toString();

        String sql = "SELECT fg.film_id, g.genre_id, g.name FROM genre fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id WHERE fg.film_id IN (" + result + ")";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

        while (sqlRowSet.next()) {
            Genre genre = new Genre();
            Long filmId = sqlRowSet.getLong("film_id");
            genre.setId(sqlRowSet.getInt("genre_id"));
            genre.setName(sqlRowSet.getString("name"));
            filmGenresMap.putIfAbsent(filmId, new TreeSet<>());
            filmGenresMap.get(filmId).add(genre);
        }
        return filmGenresMap;
    }
}
