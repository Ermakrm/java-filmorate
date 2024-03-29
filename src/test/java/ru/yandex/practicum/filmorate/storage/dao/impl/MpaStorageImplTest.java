package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MpaStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaStorageImplTest {
    private final MpaStorage mpaStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    @DirtiesContext
    void getMPAs() {
        List<MPA> mpaList = new ArrayList<>(mpaStorage.getMPAs());
        assertEquals(5, mpaList.size());
        assertEquals(mpaList.get(0), new MPA(1, "G"));
        assertEquals(mpaList.get(1), new MPA(2, "PG"));
        assertEquals(mpaList.get(2), new MPA(3, "PG-13"));
        assertEquals(mpaList.get(3), new MPA(4, "R"));
        assertEquals(mpaList.get(4), new MPA(5, "NC-17"));
    }

    @Test
    @DirtiesContext
    void getMpaById() {
        assertEquals(mpaStorage.getMpaById(1), new MPA(1, "G"));
        assertEquals(mpaStorage.getMpaById(2), new MPA(2, "PG"));
        assertEquals(mpaStorage.getMpaById(3), new MPA(3, "PG-13"));
        assertEquals(mpaStorage.getMpaById(4), new MPA(4, "R"));
        assertEquals(mpaStorage.getMpaById(5), new MPA(5, "NC-17"));
        assertThrows(InvalidDataAccessApiUsageException.class, () -> mpaStorage.getMpaById(999));
    }

    @Test
    @DirtiesContext
    void updateFilmMPA() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100)
                .mpa(new MPA(1, "G")).build();
        film = filmDbStorage.addFilm(film);
        assertEquals(mpaStorage.getMpaById(film.getMpa().getId()), film.getMpa());
        film.setMpa(new MPA(3, "PG-13"));
        filmDbStorage.updateFilm(film);
        assertEquals(mpaStorage.getMpaById(film.getMpa().getId()), film.getMpa());
    }
}