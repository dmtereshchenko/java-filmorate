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
import ru.yandex.practicum.filmorate.storage.database.DataBaseFilmStorage;
import ru.yandex.practicum.filmorate.storage.database.DataBaseFriendshipStorage;
import ru.yandex.practicum.filmorate.storage.database.DataBaseMpaAndGenresStorage;
import ru.yandex.practicum.filmorate.storage.database.DataBaseUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FilmorateApplicationTest {
    @Autowired
    private final DataBaseUserStorage dataBaseUserStorage;
    @Autowired
    private final DataBaseFilmStorage dataBaseFilmStorage;
    @Autowired
    private final DataBaseFriendshipStorage dataBaseFriendshipStorage;
    @Autowired
    private final DataBaseMpaAndGenresStorage dataBaseMpaAndGenresStorage;

    @Test
    public void testCreateAndGetUser() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        int i = dataBaseUserStorage.add(user);
        user.setId(i);
        Optional<User> userFromDB = dataBaseUserStorage.getById(i);
        Assertions.assertThat(userFromDB)
                .isPresent();
        Assertions.assertThat(userFromDB.get())
                .isEqualTo(user);
    }

    @Test
    public void testUpdateUser() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        int i = dataBaseUserStorage.add(user);
        User userUpdated = new User(i, "doloreUpdate", "est adipisicing", "mail@yandex.ru", LocalDate.of(1976, 9, 20));
        dataBaseUserStorage.update(userUpdated);
        Assertions.assertThat(dataBaseUserStorage.getById(i).get())
                .isEqualTo(userUpdated);
    }

    @Test
    public void testGetAllUsers() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        User user2 = new User("doloreUpdate", "est adipisicing", "mail@yandex.ru", LocalDate.of(1976, 9, 20));
        User user3 = new User("friend", "friend adipisicing", "friend@mail.ru", LocalDate.of(1976, 11, 12));
        user.setId(dataBaseUserStorage.add(user));
        user2.setId(dataBaseUserStorage.add(user2));
        user3.setId(dataBaseUserStorage.add(user3));
        List<User> usersFromDb = dataBaseUserStorage.getAll();
        Assertions.assertThat(usersFromDb)
                .isNotNull()
                .contains(user, user2, user3);
    }

    @Test
    public void testCreateAndGetFilm() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        int i = dataBaseFilmStorage.add(film);
        film.setId(i);
        Optional<Film> filmFromDB = dataBaseFilmStorage.getById(i);
        Assertions.assertThat(filmFromDB)
                .isPresent();
        Assertions.assertThat(filmFromDB.get())
                .isEqualTo(film);
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        int i = dataBaseFilmStorage.add(film);
        film.setId(i);
        Film filmUpdated = new Film(i, "Film Updated", "New film update description", LocalDate.of(1989, 4, 17),
                190, new Mpa(2, "PG"));
        dataBaseFilmStorage.update(filmUpdated);
        Assertions.assertThat(dataBaseFilmStorage.getById(film.getId()).get())
                .isEqualTo(filmUpdated);
    }

    @Test
    public void testGetAllFilms() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        Film film2 = new Film("Film Updated", "New film update description", LocalDate.of(1989, 4, 17),
                190, new Mpa(2, "PG"));
        Film film3 = new Film("New film", "New film about friends", LocalDate.of(1999, 4, 30), 120, (new Mpa(3, "PG-13")));
        film.addGenre(new Genre(1, "Комедия"));
        film2.addGenre(new Genre(1, "Комедия"));
        film2.addGenre(new Genre(2, "Драма"));
        film2.addGenre(new Genre(3, "Мультфильм"));
        film.setId(dataBaseFilmStorage.add(film));
        film2.setId(dataBaseFilmStorage.add(film2));
        film3.setId(dataBaseFilmStorage.add(film3));
        dataBaseMpaAndGenresStorage.addGenres(film);
        dataBaseMpaAndGenresStorage.addGenres(film2);
        List<Film> filmsFromDB = dataBaseFilmStorage.getAll();
        Assertions.assertThat(filmsFromDB)
                .isNotNull()
                .contains(film, film2, film3);
    }

    @Test
    public void testAddAndGetFriend() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        User user2 = new User("doloreUpdate", "est adipisicing", "mail@yandex.ru", LocalDate.of(1976, 9, 20));
        user.setId(dataBaseUserStorage.add(user));
        user2.setId(dataBaseUserStorage.add(user2));
        dataBaseFriendshipStorage.add(user.getId(), user2.getId());
        List<Integer> userFriends = dataBaseFriendshipStorage.getFriendList(user.getId());
        Assertions.assertThat(userFriends)
                .isNotNull();
        Assertions.assertThat(userFriends.get(0))
                .isEqualTo(user2.getId());
    }

    @Test
    public void testRemoveFriend() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        User user2 = new User("doloreUpdate", "est adipisicing", "mail@yandex.ru", LocalDate.of(1976, 9, 20));
        user.setId(dataBaseUserStorage.add(user));
        user2.setId(dataBaseUserStorage.add(user2));
        dataBaseFriendshipStorage.add(user.getId(), user2.getId());
        dataBaseFriendshipStorage.remove(user.getId(), user2.getId());
        List<Integer> userFriends = dataBaseFriendshipStorage.getFriendList(user.getId());
        Assertions.assertThat(userFriends)
                .isEmpty();
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> allGenres = dataBaseMpaAndGenresStorage.getAllGenres();
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
        List<Mpa> allCategories = dataBaseMpaAndGenresStorage.getAllCategories();
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
        Assertions.assertThat(dataBaseMpaAndGenresStorage.getGenre(id1).get())
                .isEqualTo(genre1);
        Assertions.assertThat(dataBaseMpaAndGenresStorage.getGenre(id2).get())
                .isEqualTo(genre2);
    }

    @Test
    public void testGetCategory() {
        int id1 = 1;
        int id2 = 2;
        Mpa mpa1 = new Mpa(id1, "G");
        Mpa mpa2 = new Mpa(id2, "PG");
        Assertions.assertThat(dataBaseMpaAndGenresStorage.getCategory(id1).get())
                .isEqualTo(mpa1);
        Assertions.assertThat(dataBaseMpaAndGenresStorage.getCategory(id2).get())
                .isEqualTo(mpa2);
    }

    @Test
    public void testAddAndGetGenres() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        Genre genre1 = new Genre(1, "Комедия");
        Genre genre2 = new Genre(2, "Драма");
        Genre genre3 = new Genre(3, "Мультфильм");
        film.setId(dataBaseFilmStorage.add(film));
        film.addGenre(genre1);
        film.addGenre(genre2);
        film.addGenre(genre3);
        dataBaseMpaAndGenresStorage.addGenres(film);
        Set<Genre> genres = dataBaseMpaAndGenresStorage.getGenresByFilmId(film.getId());
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
        film.setId(dataBaseFilmStorage.add(film));
        film.addGenre(genre1);
        film.addGenre(genre2);
        film.addGenre(genre3);
        dataBaseMpaAndGenresStorage.addGenres(film);
        dataBaseMpaAndGenresStorage.removeGenres(film.getId());
        Set<Genre> genres = dataBaseMpaAndGenresStorage.getGenresByFilmId(film.getId());
        Assertions.assertThat(genres)
                .isEmpty();
    }

    @Test
    public void testUserExists() {
        User user = new User("dolore", "Nick Name", "mail@mail.ru", LocalDate.of(1946, 8, 20));
        user.setId(dataBaseUserStorage.add(user));
        Assertions.assertThat(dataBaseUserStorage.exists(user.getId()))
                .isTrue();
    }

    @Test
    public void testFilmExists() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1967, 3, 25), 100,
                new Mpa(1, "G"));
        film.setId(dataBaseFilmStorage.add(film));
        Assertions.assertThat(dataBaseFilmStorage.exists(film.getId()))
                .isTrue();
    }

    @Test
    public void testCategoryExists() {
        Assertions.assertThat(dataBaseMpaAndGenresStorage.categoryExists(0))
                .isFalse();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.categoryExists(1))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.categoryExists(2))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.categoryExists(3))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.categoryExists(4))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.categoryExists(5))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.categoryExists(6))
                .isFalse();
    }

    @Test
    public void testGenreExists() {
        Assertions.assertThat(dataBaseMpaAndGenresStorage.genreExists(0))
                .isFalse();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.genreExists(1))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.genreExists(2))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.genreExists(3))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.genreExists(4))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.genreExists(5))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.genreExists(6))
                .isTrue();
        Assertions.assertThat(dataBaseMpaAndGenresStorage.genreExists(7))
                .isFalse();
    }
}
