package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaStorageDb {
    private final JdbcTemplate jdbcTemplate;

    public Mpa findById(long id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (!userRows.next()) {
            throw new NotFoundException("There is no mpa with this id");
        }
        Mpa mpa = new Mpa();
        mpa.setId(userRows.getInt("mpa_id"));
        mpa.setName(userRows.getString("name"));
        return mpa;
    }

    public List<Mpa> findAll() {
        String sql = "SELECT * FROM mpa";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sql);
        List<Mpa> mpasList = new ArrayList<>();
        while (mpaRows.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaRows.getInt("mpa_id"));
            mpa.setName(mpaRows.getString("name"));
            mpasList.add(mpa);
        }
        return mpasList;
    }
}
