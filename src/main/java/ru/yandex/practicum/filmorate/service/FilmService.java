package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();
    private static Integer generatorFilmId = 0;

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} успешно добавлен", film.getName());
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId()) || film.getId() == null) {
            throw new ValidationException("Ошибка обновления! Такого фильма не существует");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм {} успешно обновлен", film.getName());
        return film;
    }

    private static Integer getNextId() {
        return ++generatorFilmId;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getDuration() == null ||
                film.getReleaseDate() == null || film.getDescription() == null) {
            log.error("Одно или несколько полей - null");
            throw new ValidationException("Заполнены не все данные");
        }
        if (film.getName().isBlank()) {
            log.error("Пустое название фильма");
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Длина описания больше 200 символов");
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Некорректная дата релиза");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.error("Отрицательная продолжительность");
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
