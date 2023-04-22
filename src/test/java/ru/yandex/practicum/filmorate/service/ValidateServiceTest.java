package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateServiceTest {

    ValidateService validateService = new ValidateService();
    private static Validator validator;

    static  {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void filmNameShouldNotBeBlank() {
        Film film = new Film("testName", "testDescription", LocalDate.now(), 30);
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Есть возможность создать фильм с пустым названием.");
    }

    @Test
    void filmDescriptionShouldNotBeMoreThan200() {
        Film film = new Film("testName", "testDescription", LocalDate.now(), 30);
        StringBuilder sb = new StringBuilder("oipasjhguhauisghauighsaiughaiuoshgoiuaghiuashguiashgiahgauigshaiughaksjghajkgha" +
                "jksghakjghaksjghakjsghakjpghsakjghaskpgjhaskgjhaspkgjhaspgkjhasgpkjashgjksahgasjkghaskjpghapskgasskdgajdsiogj" +
                "aoisdgjaoisgaoisjdgioasgjoiasghjoaipsghjaoisgjpoaisgjag");
        assertTrue(sb.length() > 200);
        String descriptionMoreThan200 = sb.toString();
        film.setDescription(descriptionMoreThan200);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Есть возможность создать фильм с описанием более 200 символов.");
    }

    @Test
    void filmReleaseDateShouldNotBeToEarly() {
        Film film = new Film("testName", "testDescription", LocalDate.now(), 30);
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> validateService.validateFilm(film), "Некорректная работа валидатора с некорректной датой.");
    }

    @Test
    void filmDurationShouldBePositive() {
        Film film = new Film("testName", "testDescription", LocalDate.now(), 30);
        film.setDuration(Duration.ofMinutes(0));
        assertThrows(ValidationException.class, () -> validateService.validateFilm(film), "Некорректная работа валидатора с некорректной продолжительностью.");
    }

    @Test
    void userEmailWithoutDogShouldFail() {
        User user = new User("test@test.com", "testLogin", "testName", LocalDate.of(1990, 9, 11));
        user.setEmail("testststs.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Есть возможность создать пользователя с некорректным e-mail адресом");
    }

    @Test
    void ifNameIsEmptyNameEqualsLogin() {
        User user = new User("test@test.com", "testLogin", "testName", LocalDate.of(1990, 9, 11));
        user.setName("");
        validateService.validateUser(user);
        assertEquals(user.getLogin(), user.getName(), "Не использован логин для пользователя с пустым именем.");
    }
}