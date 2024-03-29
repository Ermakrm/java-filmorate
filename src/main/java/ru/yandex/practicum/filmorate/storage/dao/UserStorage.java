package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getUsers();

    User getUser(int id);

    User addUser(User user);

    User removeUser(User user);

    User updateUser(User user);
}
