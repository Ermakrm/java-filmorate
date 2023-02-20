package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
@Slf4j
@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        return filmService.findAll();
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) {
        log.info("Creating film {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        log.info("Updating film {}", film);
        return filmService.updateFilm(film);
    }

}
