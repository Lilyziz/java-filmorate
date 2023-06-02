package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeStorageDb {
    private final JdbcTemplate jdbcTemplate;

    public void addLike(long filmId, long userId) {
        String sql = "INSERT INTO likesList (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        updateRating(filmId);
    }

    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM likesList WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRating(filmId);
    }

    private Integer countLikes(long filmId) {
        String sql = "SELECT COUNT(*) FROM likesList WHERE film_id=";
        return jdbcTemplate.queryForObject(sql + filmId, Integer.class);
    }

    public void updateRating(long filmId) {
        long likes = countLikes(filmId);
        String sql = "UPDATE films SET rating=? WHERE film_id=?";
        jdbcTemplate.update(sql, likes, filmId);
    }
}
