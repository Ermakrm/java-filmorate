package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaStorage {
    MPA getMpaById(int id);

    List<MPA> getMPAs();
}
