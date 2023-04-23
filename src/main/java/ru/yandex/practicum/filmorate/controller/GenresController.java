package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.DataBase.DBFilmService;

import java.util.List;

@RestController
public class GenresController {

    private final DBFilmService service;

    @Autowired
    public GenresController(DBFilmService dbFilmService) {
        this.service = dbFilmService;
    }

    @GetMapping("/genres")
    List<Genre> findAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    Genre findGenreById(@PathVariable int id) {
        return service.getGenre(id);
    }
}
