package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private static Integer generatorFilmId = 0;

    private Integer getNextId() {
        return ++generatorFilmId;
    }

    public Collection<Film> getFilms() {
        return films.values();
    }


    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new IllegalArgumentException("Wrong ID");
        }
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Film added {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId()) || film.getId() == null) {
            throw new ValidationException("Update error! There is no such film");
        }
        films.put(film.getId(), film);
        log.debug("film updated {}", film);
        return film;
    }

    @Override
    public Film removeFilm(Film film) {
        if (films.containsKey(film.getId())) {
            Film deletedFilm = films.remove(film.getId());
            log.debug("Film deleted {}", film);
            return deletedFilm;
        } else {
            throw new ValidationException("Deletion error! There is no such film");
        }
    }
}
