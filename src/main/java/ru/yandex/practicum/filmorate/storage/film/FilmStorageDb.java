package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Component("filmStorageDb")
@RequiredArgsConstructor
@Primary
public class FilmStorageDb implements IFilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql);
        List<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            films.add(fillFilm(filmRows));
        }
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setString(4, Integer.toString(film.getDuration()));
            ps.setString(5, Integer.toString(film.getMpa().getId()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId(keyHolder.getKey().longValue());
        }
        updateGenresForFilmId(film.getId(), film.getGenres());
        return film;
    }

    private Mpa createMpaForFilm(Integer mpaId) {
        String sql = "SELECT name FROM mpa WHERE mpa_id = ?";
        SqlRowSet mpaName = jdbcTemplate.queryForRowSet(sql, mpaId);
        if (mpaName.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaId);
            mpa.setName(mpaName.getString("name"));
            return mpa;
        }
        throw new NotFoundException("There is no mpa with this id");
    }

    private Set<Genre> createGenresForFilm(long filmId) {
        String sql = "SELECT genre_id FROM genre WHERE film_id = ?";
        SqlRowSet genreIds = jdbcTemplate.queryForRowSet(sql, filmId);
        Set<Integer> ids = new HashSet<>();
        while (genreIds.next()) {
            ids.add(Integer.valueOf(genreIds.getString("genre_id")));
        }
        sql = "SELECT name FROM genres WHERE genre_id = ?";
        Set<Genre> genres = new TreeSet<>();
        for (Integer id : ids) {
            SqlRowSet genreName = jdbcTemplate.queryForRowSet(sql, id);
            genreName.next();
            Genre genre = new Genre();
            genre.setId(id);
            genre.setName(genreName.getString("name"));
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public Film updateFilm(Film film) {
        film.setGenres(new TreeSet<>(film.getGenres()));
        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ?, rating = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRating(),
                film.getId());
        updateGenresForFilmId(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        filmRows.next();
        Film film = fillFilm(filmRows);
        film.setGenres(createGenresForFilm(id));
        return film;
    }

    @Override
    public boolean contains(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        return jdbcTemplate.queryForRowSet(sql, id).next();
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE * FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Film fillFilm(SqlRowSet filmRows) {
        Film film = new Film();
        film.setId(filmRows.getInt("film_id"));
        film.setName(filmRows.getString("name"));
        film.setDescription(filmRows.getString("description"));
        film.setReleaseDate(filmRows.getDate("releaseDate").toLocalDate());
        film.setDuration(filmRows.getInt("duration"));
        film.setRating(filmRows.getInt("rating"));
        if (!filmRows.wasNull()) {
            film.setMpa(createMpaForFilm(filmRows.getInt("mpa_id")));
        }
        film.setGenres(createGenresForFilm(film.getId()));
        return film;
    }

    @Override
    public List<Film> getTopFilmsWithCount(long count) {
        String sql = "SELECT f.*, m.name FROM films AS f INNER JOIN mpa AS m " +
                "ON m.mpa_id = f.mpa_id ORDER BY rating DESC LIMIT ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, count);
        ArrayList<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            Film film = fillFilm(filmRows);
            films.add(film);
        }
        return films;
    }

    private void updateGenresForFilmId(long filmId, Set<Genre> genres) {
        deleteGenresForFilmId(filmId);
        String sql = "INSERT INTO genre (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    private void deleteGenresForFilmId(long filmId) {
        String sql = "DELETE FROM genre WHERE film_id=?";
        jdbcTemplate.update(sql, filmId);
    }
}

