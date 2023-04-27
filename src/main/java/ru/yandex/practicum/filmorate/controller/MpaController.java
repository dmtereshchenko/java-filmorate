package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.DataBase.DBFilmService;

import java.util.List;

@RestController
public class MpaController {

    private final DBFilmService service;

    @Autowired
    public MpaController(DBFilmService dbFilmService) {
        this.service = dbFilmService;
    }

    @GetMapping("/mpa")
    List<Mpa> findAllCategories() {
        return service.getAllCategories();
    }

    @GetMapping("/mpa/{id}")
    Mpa findCategoryById(@PathVariable int id) {
        return service.getCategory(id);
    }
}
