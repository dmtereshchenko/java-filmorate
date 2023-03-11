package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
@Slf4j
public class ValidateService {

    private static final LocalDate FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(FILM_RELEASE_DATE)) {
            log.warn("Дата выхода фильма слишком ранняя: {}", film.getReleaseDate());
            throw new ValidationException("Проверьте дату выхода фильма.");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма 0 или меньше минут: {}", film.getDuration());
            throw new ValidationException("Проверьте продолжительность фильма");
        }
    }

    public void validateUser(User user) {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        if (user.getLogin().isEmpty()) {
            log.warn("Логин пользователя пуст: {}", user.getLogin());
            throw new ValidationException("Логин пользователя пуст.");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя содержит пробел(ы): {}", user.getLogin());
            throw new ValidationException("Логин пользователя содержит пробел(ы).");
        }
    }
}
