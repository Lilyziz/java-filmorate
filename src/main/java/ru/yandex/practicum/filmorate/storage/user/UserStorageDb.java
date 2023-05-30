package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("userStorageDb")
@RequiredArgsConstructor
@Primary
public class UserStorageDb implements IUserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql);
        List<User> users = new ArrayList<>();
        while (userRows.next()) {
            users.add(fillUser(userRows));
        }
        return users;
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, user.getBirthday().toString());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
        }
        return user;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE * FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        userRows.next();
        return fillUser(userRows);
    }

    @Override
    public boolean contains(long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForRowSet(sql, id).next();
    }

    private User fillUser(SqlRowSet userRows) {
        User user = new User();
        user.setId(userRows.getInt("user_id"));
        user.setEmail(userRows.getString("email"));
        user.setLogin(userRows.getString("login"));
        user.setName(userRows.getString("name"));
        user.setBirthday(userRows.getDate("birthday").toLocalDate());
        return user;
    }

    @Override
    public void addFriend(long userId, long friend) {
        String sql = "INSERT INTO friendsList (user_id, friend_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, userId, friend);
    }

    @Override
    public List<User> findFriends(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT u.* FROM users AS u " +
                        "INNER JOIN friendsList AS f ON f.friend_id = u.user_id WHERE f.user_id = ?", id);
        List<User> friends = new ArrayList<>();
        while (userRows.next()) {
            User friend = fillUser(userRows);
            friends.add(friend);
        }
        return friends;
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "SELECT * FROM friendsList WHERE user_id=? AND friend_id=?";
        SqlRowSet userFriendRows = jdbcTemplate.queryForRowSet(sql, userId, friendId);
        sql = "DELETE FROM friendsList WHERE user_id=? AND friend_id=?";
        if (userFriendRows.next()) {
            jdbcTemplate.update(sql, userId, friendId);
        }
    }

    @Override
    public List<User> findCommonFriends(long userId, long friendId) {
        Set<User> friends1 = new HashSet<>(findFriends(userId));
        Set<User> friends2 = new HashSet<>(findFriends(friendId));
        friends1.retainAll(friends2);
        return new ArrayList<>(friends1);
    }
}
