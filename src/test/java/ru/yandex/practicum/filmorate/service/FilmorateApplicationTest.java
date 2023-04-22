package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.database.FilmDao;
import ru.yandex.practicum.filmorate.storage.database.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.database.MpaGenresDao;
import ru.yandex.practicum.filmorate.storage.database.UserDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FilmorateApplicationTest {
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final FilmDao filmDao;
    @Autowired
    private final FriendshipDao friendshipDao;
    @Autowired
    private final MpaGenresDao mpaGenresDao;

    @Test
    public void testCreateAndGetUser() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        int i = userDao.add(user);
        user.setId(i);
        Optional<User> userFromDB = userDao.getById(i);
        Assertions.assertThat(userFromDB)
                .isPresent();
        Assertions.assertThat(userFromDB.get())
                .isEqualTo(user);
    }

    @Test
    public void testUpdateUser() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        int i = userDao.add(user);
        User userUpdated = new User(i, "doloreUpdate", "est adipisicing", "mail@yandex.ru", LocalDate.of(1976, 9, 20));
        userDao.update(userUpdated);
        Assertions.assertThat(userDao.getById(i).get())
                .isEqualTo(userUpdated);
    }

    @Test
    public void testGetAllUsers() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        User user2 = new User("doloreUpdate", "est adipisicing", "mail@yandex.ru", LocalDate.of(1976, 9, 20));
        User user3 = new User("friend", "friend adipisicing", "friend@mail.ru", LocalDate.of(1976, 11, 12));
        user.setId(userDao.add(user));
        user2.setId(userDao.add(user2));
        user3.setId(userDao.add(user3));
        List<User> usersFromDb = userDao.getAll();
        Assertions.assertThat(usersFromDb)
                .isNotNull()
                .contains(user, user2, user3);
    }

    @Test
    public void testCreateAndGetFilm() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        int i = filmDao.add(film);
        film.setId(i);
        Optional<Film> filmFromDB = filmDao.getById(i);
        Assertions.assertThat(filmFromDB)
                .isPresent();
        Assertions.assertThat(filmFromDB.get())
                .isEqualTo(film);
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        int i = filmDao.add(film);
        film.setId(i);
        Film filmUpdated = new Film(i, "Film Updated", "New film update description", LocalDate.of(1989, 4, 17),
                190, new Mpa(2, "PG"));
        filmDao.update(filmUpdated);
        Assertions.assertThat(filmDao.getById(film.getId()).get())
                .isEqualTo(filmUpdated);
    }

    @Test
    public void testGetAllFilms() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        Film film2 = new Film("Film Updated", "New film update description", LocalDate.of(1989, 4, 17),
                190, new Mpa(2, "PG"));
        Film film3 = new Film("New film", "New film about friends", LocalDate.of(1999, 4, 30), 120, (new Mpa(3, "PG-13")));
        film.setId(filmDao.add(film));
        film2.setId(filmDao.add(film2));
        film3.setId(filmDao.add(film3));
        List<Film> filmsFromDB = filmDao.getAll();
        Assertions.assertThat(filmsFromDB)
                .isNotNull()
                .contains(film, film2, film3);
    }

    @Test
    public void testAddAndGetFriend() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        User user2 = new User("doloreUpdate", "est adipisicing", "mail@yandex.ru", LocalDate.of(1976, 9, 20));
        user.setId(userDao.add(user));
        user2.setId(userDao.add(user2));
        friendshipDao.add(user.getId(), user2.getId());
        List<Integer> userFriends = friendshipDao.getFriendList(user.getId());
        Assertions.assertThat(userFriends)
                .isNotNull();
        Assertions.assertThat(userFriends.get(0))
                .isEqualTo(user2.getId());
    }

    @Test
    public void testRemoveFriend() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        User user2 = new User("doloreUpdate", "est adipisicing", "mail@yandex.ru", LocalDate.of(1976, 9, 20));
        user.setId(userDao.add(user));
        user2.setId(userDao.add(user2));
        friendshipDao.add(user.getId(), user2.getId());
        friendshipDao.remove(user.getId(), user2.getId());
        List<Integer> userFriends = friendshipDao.getFriendList(user.getId());
        Assertions.assertThat(userFriends)
                .isEmpty();
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> allGenres = mpaGenresDao.getAllGenres();
        Assertions.assertThat(allGenres)
                .contains(new Genre(1, "Комедия"))
                .contains(new Genre(2, "Драма"))
                .contains(new Genre(3, "Мультфильм"))
                .contains(new Genre(4, "Триллер"))
                .contains(new Genre(5, "Документальный"))
                .contains(new Genre(6, "Боевик"));
    }

    @Test
    public void testGetAllCategories() {
        List<Mpa> allCategories = mpaGenresDao.getAllCategories();
        Assertions.assertThat(allCategories)
                .contains(new Mpa(1, "G"))
                .contains(new Mpa(2, "PG"))
                .contains(new Mpa(3, "PG-13"))
                .contains(new Mpa(4, "R"))
                .contains(new Mpa(5, "NC-17"));
    }

    @Test
    public void testGetGenre() {
        int id1 = 1;
        int id2 = 2;
        Genre genre1 = new Genre(id1, "Комедия");
        Genre genre2 = new Genre(id2, "Драма");
        Assertions.assertThat(mpaGenresDao.getGenre(id1).get())
                .isEqualTo(genre1);
        Assertions.assertThat(mpaGenresDao.getGenre(id2).get())
                .isEqualTo(genre2);
    }

    @Test
    public void testGetCategory() {
        int id1 = 1;
        int id2 = 2;
        Mpa mpa1 = new Mpa(id1, "G");
        Mpa mpa2 = new Mpa(id2, "PG");
        Assertions.assertThat(mpaGenresDao.getCategory(id1).get())
                .isEqualTo(mpa1);
        Assertions.assertThat(mpaGenresDao.getCategory(id2).get())
                .isEqualTo(mpa2);
    }

    @Test
    public void testAddAndGetGenres() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(2, "Драма");
        Genre genre3 = new Genre(3, "Мультфильм");
        film.setId(filmDao.add(film));
        film.addGenre(genre1);
        film.addGenre(genre2);
        film.addGenre(genre3);
        mpaGenresDao.addGenres(film);
        List<Integer> genresId = mpaGenresDao.getGenresByFilmId(film.getId());
        List<Genre> genres = new ArrayList<>();
        for (int i : genresId) {
            genres.add(mpaGenresDao.getGenre(i).get());
        }
        Assertions.assertThat(genres)
                .contains(genre1, genre2, genre3);
    }

    @Test
    public void testRemoveGenres() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(2, "Драма");
        Genre genre3 = new Genre(3, "Мультфильм");
        film.setId(filmDao.add(film));
        film.addGenre(genre1);
        film.addGenre(genre2);
        film.addGenre(genre3);
        mpaGenresDao.addGenres(film);
        mpaGenresDao.removeGenres(film.getId());
        List<Integer> genresId = mpaGenresDao.getGenresByFilmId(film.getId());
        Assertions.assertThat(genresId)
                .isEmpty();
    }
}
