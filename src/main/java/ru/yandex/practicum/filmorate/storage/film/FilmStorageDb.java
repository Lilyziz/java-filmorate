package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Slf4j
@Component("filmStorageDb")
@RequiredArgsConstructor
@Primary
public class FilmStorageDb implements IFilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM films";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql);
        List<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            films.add(fillFilm(filmRows));
        }
        return films;
    }

    @Override
    public Film create(Film film) {
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
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQuery = "INSERT INTO genre (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
        return film;
    }

    private Mpa createMpaForFilm(Integer mpaId) {
        String sql = "SELECT name FROM mpa WHERE mpa_id = ?";
        SqlRowSet mpaName = jdbcTemplate.queryForRowSet(sql, mpaId);
        mpaName.next();
        Mpa mpa = new Mpa();
        mpa.setId(mpaId);
        mpa.setName(mpaName.getString("name"));
        return mpa;
    }

    @Override
    public Film update(Film film) {
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
        return film;
    }

    @Override
    public Optional<Film> findById(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (filmRows.next()) {
            return Optional.of(fillFilm(filmRows));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> contains(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            return Optional.of(userRows.next());
        }
        return Optional.empty();
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
        return film;
    }

    @Override
    public List<Film> findTopFilmsWithCount(long count) {
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
}

