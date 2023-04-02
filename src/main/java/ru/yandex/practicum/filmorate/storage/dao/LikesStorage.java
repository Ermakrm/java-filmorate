package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesStorage {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getTopFilms(int count);
}
