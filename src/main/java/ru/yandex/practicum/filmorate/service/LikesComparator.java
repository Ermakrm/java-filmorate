package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

class LikesComparator implements Comparator<Film> {
    @Override
    public int compare(Film o1, Film o2) {
        return o2.getLikes().size() - o1.getLikes().size();
    }
}
