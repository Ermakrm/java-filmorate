package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.dao.GenresStorage;

import java.util.List;

@Repository
public class GenresStorageImpl implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenresStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genres> getGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", (rs, rowNum) -> {
            Genres g = new Genres();
            g.setId(rs.getInt("GENRE_ID"));
            g.setName(rs.getString("NAME"));
            return g;
        });
    }

    @Override
    public Genres getGenreById(int id) {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE GENRE_ID = ?", id);
        if (genresRows.next()) {
            Genres g = new Genres();
            g.setId(genresRows.getInt("GENRE_ID"));
            g.setName(genresRows.getString("NAME"));
            return g;
        } else {
            throw new IllegalArgumentException("WRONG ID");
        }
    }

    @Override
    public List<Genres> getGenresByFilmId(int id) {
        String sql = "SELECT * FROM GENRES LEFT JOIN FILM_GENRES FG on GENRES.GENRE_ID = FG.GENRE_ID " +
                "WHERE FILM_ID = " + id;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Genres g = new Genres();
            g.setId(rs.getInt("GENRE_ID"));
            g.setName(rs.getString("NAME"));
            return g;
        });
    }

    @Override
    public void addFilmGenre(int filmId, int genreId) {
        int status = jdbcTemplate.update("INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) " +
                "VALUES ( ?, ? )", filmId, genreId);
        if (status != 1) throw new IllegalArgumentException("WRONG");
    }

    @Override
    public void deleteFilmGenres(int filmId) {
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", filmId);
    }
}
