package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserControllerTest {
    UserController userController;

    @BeforeEach
    void beforeEach(){
        userController = new UserController(new UserService());
    }

    @Test
    void createUser(){
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        assertEquals(user, userController.createUser(user));
    }

    @Test
    void updateUser(){
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        assertEquals(user, userController.createUser(user));
        String newName = "newName";
        user.setName(newName);
        String newEmail = "newEmail@yandex.ru";
        user.setEmail(newEmail);
        User updatedUser = userController.updateUser(user);
        assertEquals(newName, updatedUser.getName());
        assertEquals(newEmail, updatedUser.getEmail());
    }

    @Test
    void getAllUsers(){
        int len = userController.getAllUsers().size();
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        User user2 = User.builder().email("email2@yandex.ru").login("login2").name("name2")
                .birthday(LocalDate.of(2001, 2, 2)).build();
        userController.createUser(user);
        userController.createUser(user2);
        Collection<User> users = userController.getAllUsers();
        assertEquals(len + 2, users.size());
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
    }

    @Test
    void createUserWithWrongEmail(){
        User user = User.builder().email(null).login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setEmail("emailyandex.ru");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void createUserWithWrongLogin(){
        User user = User.builder().email("email@yandex.ru").login(null).name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setLogin("log in");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void createUserWithoutName(){
        User user = User.builder().email("email@yandex.ru").login("login").name(null)
                .birthday(LocalDate.of(2000, 1, 1)).build();
        User createdUser = userController.createUser(user);
        assertEquals(user.getLogin(), createdUser.getName());
        user.setName("");
        createdUser = userController.updateUser(user);
        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    void createUserWithWrongBirthdate(){
        LocalDate now = LocalDate.now();
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(null).build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setBirthday(now.plusDays(1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        user.setBirthday(now);
        assertEquals(user, userController.createUser(user));
    }

    @Test
    void updateUserWithWrongId(){
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        userController.createUser(user);
        user.setId(999);
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }
}