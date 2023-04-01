package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendsStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class FriendsStorageImpl implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getFriends(int id) {
        String sql = "SELECT * FROM FRIENDS AS F JOIN USERS U on F.FRIEND_ID = U.USER_ID WHERE F.USER_ID = " + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getInt("FRIEND_ID"));
            u.setLogin(rs.getString("LOGIN"));
            u.setName(rs.getString("NAME"));
            u.setEmail(rs.getString("EMAIL"));
            u.setBirthday(LocalDate.parse(rs.getString("BIRTHDAY")));
            return u;
        });
    }

    @Override
    public void addFriend(int id1, int id2) {
        int status = jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)", id1, id2);
        if (status != 1) throw new IllegalArgumentException("WRONG ID");
    }

    @Override
    public void deleteFriend(int id1, int id2) {
        jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", id1, id2);
    }

    @Override
    public List<User> getCommonFriends(int id1, int id2) {
        Set<User> common = new HashSet<>(getFriends(id1));
        common.retainAll(getFriends(id2));
        return new ArrayList<>(common);
    }
}
