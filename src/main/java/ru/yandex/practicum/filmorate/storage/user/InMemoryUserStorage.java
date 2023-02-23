package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private static Integer generatorUserId = 0;

    public Collection<User> getUsersList() {
        return users.values();
    }

    private static Integer getNextId() {
        return ++generatorUserId;
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user);
        log.debug("User added {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId()) || user.getId() == null) {
            throw new ValidationException("Ошибка обновления! Такого пользователя не существует");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("User updated {}", user);
        return user;
    }

    @Override
    public User removeUser(User user) {
        if (users.containsKey(user.getId())) {
            log.debug("User deleted {}", user);
            return users.remove(user.getId());
        } else {
            throw new ValidationException("Ошибка! Такого пользователя не существует");
        }
    }
}
