package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    static UserController userController;
    static Validator validator;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void createUser() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        assertEquals(user, userController.createUser(user));
    }

    @Test
    void updateUser() {
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
    void getAllUsers() {
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
    void createUserWithWrongEmail() {
        User user = User.builder().email(null).login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "email = null");
        user.setEmail("");
        Set<ConstraintViolation<User>> violations2 = validator.validate(user);
        assertEquals(1, violations2.size(), "email without @");
        user.setEmail("emailyandex.ru");
        Set<ConstraintViolation<User>> violations3 = validator.validate(user);
        assertEquals(1, violations3.size(), "Wrong Email");
    }

    @Test
    void createUserWithWrongLogin() {
        User user = User.builder().email("email@yandex.ru").login(null).name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "login = null");
        user.setLogin("");
        Set<ConstraintViolation<User>> violations2 = validator.validate(user);
        assertEquals(1, violations2.size(), "empty login");
        user.setLogin("log in");
        Set<ConstraintViolation<User>> violations3 = validator.validate(user);
        assertEquals(1, violations3.size(), "login with space");
    }

    @Test
    void createUserWithoutName() {
        User user = User.builder().email("email@yandex.ru").login("login").name(null)
                .birthday(LocalDate.of(2000, 1, 1)).build();
        User createdUser = userController.createUser(user);
        assertEquals(user.getLogin(), createdUser.getName());
        user.setName("");
        createdUser = userController.updateUser(user);
        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    void createUserWithWrongBirthdate() {
        LocalDate now = LocalDate.now();
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(null).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "birthday = null");
        user.setBirthday(now.plusDays(1));
        Set<ConstraintViolation<User>> violations2 = validator.validate(user);
        assertEquals(1, violations2.size(), "birthday is tommorow");
        user.setBirthday(now);
        assertEquals(user, userController.createUser(user));
    }

    @Test
    void updateUserWithWrongId() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        userController.createUser(user);
        user.setId(999);
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }

    @Test
    void addFriend() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        User user2 = User.builder().email("email2@yandex.ru").login("login2").name("name2")
                .birthday(LocalDate.of(2001, 2, 2)).build();
        userController.createUser(user);
        userController.createUser(user2);
        userController.addFriend(user.getId(), user2.getId());
        assertEquals(1, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
        assertTrue(user.getFriends().contains(user2.getId()));
        assertTrue(user2.getFriends().contains(user.getId()));
    }

    @Test
    void addFriendWithWrongId() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        userController.createUser(user);
        assertThrows(IllegalArgumentException.class, () -> userController.addFriend(user.getId(), 999));
        assertThrows(IllegalArgumentException.class, () -> userController.addFriend(999, user.getId()));
    }

    @Test
    void deleteFriend() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        User user2 = User.builder().email("email2@yandex.ru").login("login2").name("name2")
                .birthday(LocalDate.of(2001, 2, 2)).build();
        userController.createUser(user);
        userController.createUser(user2);
        userController.addFriend(user.getId(), user2.getId());
        assertEquals(1, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
        userController.deleteFriend(user.getId(), user2.getId());
        assertEquals(0, user.getFriends().size());
        assertEquals(0, user2.getFriends().size());
    }

    @Test
    void deleteFriendWithWrongId() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        User user2 = User.builder().email("email2@yandex.ru").login("login2").name("name2")
                .birthday(LocalDate.of(2001, 2, 2)).build();
        userController.createUser(user);
        userController.createUser(user2);
        userController.addFriend(user.getId(), user2.getId());
        assertEquals(1, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
        assertThrows(IllegalArgumentException.class, () -> userController.deleteFriend(user.getId(), 999));
        assertThrows(IllegalArgumentException.class, () -> userController.deleteFriend(999, user2.getId()));
        assertEquals(1, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
    }

    @Test
    void getFriends() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        User user2 = User.builder().email("email2@yandex.ru").login("login2").name("name2")
                .birthday(LocalDate.of(2001, 2, 2)).build();
        User user3 = User.builder().email("email3@yandex.ru").login("login3").name("name3")
                .birthday(LocalDate.of(2002, 3, 3)).build();
        userController.createUser(user);
        userController.createUser(user2);
        userController.createUser(user3);
        userController.addFriend(user.getId(), user2.getId());
        userController.addFriend(user.getId(), user3.getId());
        assertEquals(2, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
        assertEquals(1, user3.getFriends().size());
        assertTrue(user.getFriends().contains(user2.getId()));
        assertTrue(user.getFriends().contains(user3.getId()));
    }

    @Test
    void getFriendsWithWrongId() {
        assertThrows(IllegalArgumentException.class, () -> userController.getFriends(999));
    }

    @Test
    void getCommonFriends() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        User user2 = User.builder().email("email2@yandex.ru").login("login2").name("name2")
                .birthday(LocalDate.of(2001, 2, 2)).build();
        User user3 = User.builder().email("email3@yandex.ru").login("login3").name("name3")
                .birthday(LocalDate.of(2002, 3, 3)).build();
        User user4 = User.builder().email("email4@yandex.ru").login("login4").name("name4")
                .birthday(LocalDate.of(2002, 3, 3)).build();
        userController.createUser(user);
        userController.createUser(user2);
        userController.createUser(user3);
        userController.createUser(user4);
        userController.addFriend(user.getId(), user2.getId());
        userController.addFriend(user.getId(), user3.getId());
        userController.addFriend(user4.getId(), user2.getId());
        userController.addFriend(user4.getId(), user3.getId());
        List<User> commonUsers = userController.getCommonUsers(user4.getId(), user.getId());
        assertEquals(2, commonUsers.size());
        assertTrue(commonUsers.contains(user2));
        assertTrue(commonUsers.contains(user3));
    }

    @Test
    void getCommonFriendsWithWrongId() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        userController.createUser(user);
        assertThrows(IllegalArgumentException.class, () -> userController.getCommonUsers(user.getId(), 999));
        assertThrows(IllegalArgumentException.class, () -> userController.getCommonUsers(999, user.getId()));
        assertThrows(IllegalArgumentException.class, () -> userController.getCommonUsers(999, 111));
    }

    @Test
    void getUserById() {
        User user = User.builder().email("email@yandex.ru").login("login").name("name")
                .birthday(LocalDate.of(2000, 1, 1)).build();
        userController.createUser(user);
        assertEquals(user, userController.getUser(user.getId()));
    }

    @Test
    void getUserWithWrongId() {
        assertThrows(IllegalArgumentException.class, () -> userController.getUser(999));
    }
}