package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film addFilm(Film film);

    Film removeFilm(Film film);

    Film updateFilm(Film film);
}
