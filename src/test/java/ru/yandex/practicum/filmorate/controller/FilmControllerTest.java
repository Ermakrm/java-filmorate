package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    static FilmController filmController;
    static Validator validator;

    @BeforeEach
    void beforeAll() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void createFilm() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        assertEquals(film, filmController.createFilm(film));
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    void updateFilm() {
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
    void getAllFilms() {
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
    void createFilmWithWrongName() {
        Film film = Film.builder().name(null).description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Name is empty");
        Film film2 = Film.builder().name("").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film2);
        assertEquals(1, violations1.size(), "Name is empty");
    }

    @Test
    void createFilmWithWrongReleaseDate() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(1895, 12, 27)).duration(100).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Wrong date");
        film.setReleaseDate(null);
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film);
        assertEquals(1, violations1.size(), "Wrong date");
        Film film2 = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(1895, 12, 28)).duration(100).build();
        assertEquals(film2, filmController.createFilm(film2));
    }

    @Test
    void createFilmWithWrongDescription() {
        // 201 chars
        String description = "description/description/description/description/description/description/description/" +
                "description/description/description/description/description/description/description/description/";
        Film film = Film.builder().name("name").description(description)
                .releaseDate(LocalDate.of(1895, 12, 27)).duration(100).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Wrong description");
        film.setDescription(null);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film);
        assertEquals(2, violations2.size(), "description = null");
        // 200 chars
        String description2 = "description/description/description/description/description/description/description/" +
                "description/description/description/description/description/description/description/description";
        Film film2 = Film.builder().name("name").description(description2)
                .releaseDate(LocalDate.of(1895, 12, 28)).duration(100).build();
        assertEquals(film2, filmController.createFilm(film2));
    }

    @Test
    void createFilmWithWrongDuration() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(-1).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Wrong duration");
        film.setDuration(null);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film);
        assertEquals(1, violations2.size(), "Duration = null");
        Film film2 = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(0).build();
        assertEquals(film2, filmController.createFilm(film2));
    }

    @Test
    void updateFilmWithWrongId() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        assertEquals(film, filmController.createFilm(film));
        film.setId(999);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
    }

    @Test
    void likeFilm() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        filmController.createFilm(film);
        filmController.like(film.getId(), 1);
        filmController.like(film.getId(), 2);
        filmController.like(film.getId(), 3);
        assertEquals(3, film.getLikes().size());
        assertTrue(film.getLikes().contains(1));
        assertTrue(film.getLikes().contains(2));
        assertTrue(film.getLikes().contains(3));
    }

    @Test
    void likeFilmWithWrongId() {
        assertThrows(IllegalArgumentException.class, () -> filmController.like(999, 111));
    }

    @Test
    void deleteLike() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        filmController.createFilm(film);
        filmController.like(film.getId(), 1);
        filmController.like(film.getId(), 2);
        filmController.like(film.getId(), 3);
        assertEquals(3, film.getLikes().size());
        filmController.deleteLike(film.getId(), 1);
        assertEquals(2, film.getLikes().size());
        filmController.deleteLike(film.getId(), 2);
        assertEquals(1, film.getLikes().size());
        filmController.deleteLike(film.getId(), 3);
        assertEquals(0, film.getLikes().size());
    }

    @Test
    void deleteLikeWithWrongId() {
        assertThrows(IllegalArgumentException.class, () -> filmController.deleteLike(999, 111));
    }

    @Test
    void getPopularFilms() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        Film film2 = Film.builder().name("name2").description("description2")
                .releaseDate(LocalDate.of(2001, 2, 2)).duration(101).build();
        Film film3 = Film.builder().name("name3").description("description3")
                .releaseDate(LocalDate.of(2002, 2, 2)).duration(102).build();
        Film film4 = Film.builder().name("name4").description("description4")
                .releaseDate(LocalDate.of(2003, 3, 3)).duration(103).build();
        filmController.createFilm(film);
        filmController.createFilm(film2);
        filmController.createFilm(film3);
        filmController.createFilm(film4);
        filmController.like(film.getId(), 1);
        filmController.like(film2.getId(), 1);
        filmController.like(film2.getId(), 2);
        filmController.like(film3.getId(), 1);
        filmController.like(film3.getId(), 2);
        filmController.like(film3.getId(), 3);
        filmController.like(film3.getId(), 4);
        List<Film> topFilms = filmController.getPopularFilms("100");
        assertEquals(4, topFilms.size());
        assertEquals(film3, topFilms.get(0));
        assertEquals(film2, topFilms.get(1));
        assertEquals(film, topFilms.get(2));
        assertEquals(film4, topFilms.get(3));
    }

    @Test
    void getFilmById() {
        Film film = Film.builder().name("name").description("description")
                .releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        filmController.createFilm(film);
        assertEquals(film, filmController.getFilm(film.getId()));
    }

    @Test
    void getFilmWithWrongId() {
        assertThrows(IllegalArgumentException.class, () -> filmController.getFilm(999));
    }

}