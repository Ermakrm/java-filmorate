package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MpaStorage;

import java.util.List;

@Repository
public class MpaStorageImpl implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getMPAs() {
        return jdbcTemplate.query("SELECT * FROM MPA", (rs, rowNum) -> {
            MPA m = new MPA();
            m.setId(rs.getInt("MPA_ID"));
            m.setName(rs.getString("NAME"));
            return m;
        });
    }

    @Override
    public MPA getMpaById(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE MPA_ID = ?", id);
        if (mpaRows.next()) {
            return new MPA(mpaRows.getInt("MPA_ID"), mpaRows.getString("NAME"));
        } else {
            throw new IllegalArgumentException("WRONG ID");
        }
    }


}
