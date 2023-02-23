package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
public class UserService {

    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User getUser(int id) {
        if (!inMemoryUserStorage.getUsers().containsKey(id)) {
            throw new IllegalArgumentException("Wrong ID");
        }
        return inMemoryUserStorage.getUsers().get(id);
    }

    public User createUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getUsersList();
    }

    public void addFriend(int id1, int id2) {
        /* Долго мучался, так и не понял почему так. Если поменять две эти строки местами, то падают 2 теста
        в постмане на получение друзей пользователя с ID 1) Не знаешь, почему так может быть? */
        getUser(id2).getFriends().add(id1);
        getUser(id1).getFriends().add(id2);
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>();
        if (getUser(id).getFriends().isEmpty()) {
            return friends;
        }
        for (Integer friendId : getUser(id).getFriends()) {
            friends.add(getUser(friendId));
        }
        return friends;
    }

    public void deleteFriend(int id1, int id2) {
        if (!getUser(id1).getFriends().contains(id2)) {
            throw new IllegalArgumentException("wrong ID");
        }
        getUser(id1).getFriends().remove(id2);
        getUser(id2).getFriends().remove(id1);
    }

    public List<User> getCommonFriends(int id1, int id2) {
        // тут логическое умножение, чтобы проверить, что оба ID существуют
        if (getUser(id1).getFriends().isEmpty() && getUser(id2).getFriends().isEmpty()) {
            return List.of();
        }
        Set<Integer> commonIds = new HashSet<>(inMemoryUserStorage.getUsers().get(id1).getFriends());
        commonIds.retainAll(inMemoryUserStorage.getUsers().get(id2).getFriends());
        if (commonIds.isEmpty()) {
            return List.of();
        }
        List<User> commonFriends = new ArrayList<>();
        for (Integer id : commonIds) {
            commonFriends.add(inMemoryUserStorage.getUsers().get(id));
        }
        return commonFriends;
    }
}
