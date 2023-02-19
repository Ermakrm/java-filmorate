package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class FilmControllerTest {
    static FilmController filmController;

    @BeforeEach
    void beforeAll(){
        filmController= new FilmController(new FilmService());
    }

    @Test
    void createFilm(){
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        assertEquals(film, filmController.createFilm(film));
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void updateFilm(){
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        assertEquals(film, filmController.createFilm(film));
        assertEquals(1, filmController.getAllFilms().size());
        String newName = "newName";
        String newDescription = "newDescription";
        film.setName(newName);
        film.setDescription(newDescription);
        Film updatedFilm = filmController.updateFilm(film);
        assertEquals(newName, updatedFilm.getName());
        assertEquals(newDescription, updatedFilm.getDescription());
    }

    @Test
    void getAllFilms(){
        int len = filmController.getAllFilms().size();
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        Film film2 = Film.builder().name("name2").description("description2")
                .releaseDate(LocalDate.of(2001, 2, 2)).duration(10).build();
        filmController.createFilm(film);
        filmController.createFilm(film2);
        Collection<Film> films = filmController.getAllFilms();
        assertTrue(films.contains(film));
        assertTrue(films.contains(film2));
        assertEquals(len + 2, films.size());
    }

    @Test
    void createFilmWithWrongName(){
        Film film = Film.builder().name(null).description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Film film2 = Film.builder().name("").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(film2));
    }

    @Test
    void createFilmWithWrongReleaseDate(){
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(1895, 12, 27)).duration(100).build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        film.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Film film2 = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(1895, 12, 28)).duration(100).build();
        assertEquals(film2, filmController.createFilm(film2));
    }

    @Test
    void createFilmWithWrongDescription(){
        // 201 chars
        String description = "description/description/description/description/description/description/description/" +
                "description/description/description/description/description/description/description/description/";
        Film film = Film.builder().name("name").description(description)
                .releaseDate(LocalDate.of(1895, 12, 27)).duration(100).build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        film.setDescription(null);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        // 200 chars
        String description2 = "description/description/description/description/description/description/description/" +
                "description/description/description/description/description/description/description/description";
        Film film2 = Film.builder().name("name").description(description2)
                .releaseDate(LocalDate.of(1895, 12, 28)).duration(100).build();
        assertEquals(film2, filmController.createFilm(film2));
    }

    @Test
    void createFilmWithWrongDuration(){
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(-1).build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        film.setDuration(null);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        Film film2 = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(0).build();
        assertEquals(film2, filmController.createFilm(film2));
    }

    @Test
    void updateFilmWithWrongId(){
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        assertEquals(film, filmController.createFilm(film));
        film.setId(999);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
    }
}