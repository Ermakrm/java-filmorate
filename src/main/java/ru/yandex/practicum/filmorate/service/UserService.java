package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserService {
    private static final Map<Integer, User> users = new HashMap<>();
    private static Integer generatorUserId = 0;

    private static Integer getNextId() {
        return ++generatorUserId;
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public User createUser(User user) {
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь {} успешно добавлен", user.getName());
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId()) || user.getId() == null) {
            throw new ValidationException("Ошибка обновления! Такого пользователя не существует");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь {} успешно обновлен", user.getName());
        return user;
    }


    private void validateUser(User user) {
        if (user.getEmail() == null || user.getLogin() == null || user.getBirthday() == null) {
            log.error("Одно или несколько полей - null");
            throw new ValidationException("Заполнены не все данные");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Некорректный email");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Некорректный email");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Некорректная дата рождения");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
