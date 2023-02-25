package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film getFilm(int id) {
        if (!inMemoryFilmStorage.getFilms().containsKey(id)) {
            throw new IllegalArgumentException("Wrong ID");
        }
        return inMemoryFilmStorage.getFilms().get(id);
    }

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Collection<Film> findAllFilms() {
        return inMemoryFilmStorage.getFilmsList();
    }

    public void addLike(int filmId, int userId) {
        getFilm(filmId).getLikes().add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (!getFilm(filmId).getLikes().contains(userId)) {
            throw new IllegalArgumentException("you didn't like this movie");
        }
        inMemoryFilmStorage.getFilms().get(filmId).getLikes().remove(userId);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> films = inMemoryFilmStorage.getFilmsList().stream().sorted(
                new LikesComparator()).collect(Collectors.toList());
        if (count > films.size()) {
            count = films.size();
        }
        return films.subList(0, count);
    }
}