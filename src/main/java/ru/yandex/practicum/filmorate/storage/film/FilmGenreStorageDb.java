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
        final String sql = "SELECT g.genre_id, name FROM genre fg LEFT JOIN genres g ON " +
                            "fg.genre_id = g.genre_id WHERE film_id = ?";
        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(sql, filmId);
        Set<Genre> genres = new TreeSet<>();
        while (genresRowSet.next()){
            Genre genre = new Genre();
            genre.setId(genresRowSet.getInt("genre_id"));
            genre.setName(genresRowSet.getString("name"));
            genres.add(genre);
        }
        return genres;
    }

    public Map<Integer, Set<Genre>> getAllFilmGenres(List<Film> films) {
        Map<Integer, Set<Genre>> filmGenresMap = new HashMap<>();
        Collection<String> ids = films.stream().map(film -> String.valueOf(film.getId())).collect(Collectors.toList());
        System.out.println(ids);
        final String sql = "SELECT fg.film_id, g.genre_id, g.name FROM genre fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id WHERE fg.film_id IN (ids)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, ids);

        System.out.println(sqlRowSet);

        while (sqlRowSet.next()) {
            Genre genre = new Genre();
            Integer filmId = sqlRowSet.getInt("film_id");
            genre.setId(sqlRowSet.getInt("genre_id"));
            genre.setName(sqlRowSet.getString("name"));

            filmGenresMap.putIfAbsent(filmId, new TreeSet<>());
            filmGenresMap.get(filmId).add(genre);
        }

        return filmGenresMap;
        //jdbcTemplate.query(sql, (rs) -> {
        //    final Film film = (Film) filmGenresMap.get(rs.getInt("FILM_ID"));
        //    Genre genre = new Genre();
        //    genre.setId(rs.getInt("genre_id"));
        //    genre.setName(rs.getString("name"));
        //    filmGenresMap.get(film.getId()).add(genre);
        //}, films.stream().map(Film::getId).toArray());
        //return filmGenresMap;
    }
}
