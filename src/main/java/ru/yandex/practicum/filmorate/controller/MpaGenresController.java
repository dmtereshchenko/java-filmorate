package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.DataBase.AdditionalService;

import java.util.List;

@RestController
@Slf4j
public class MpaGenresController {
    private final AdditionalService service;

    @Autowired
    public MpaGenresController(AdditionalService service) {
        this.service = service;
    }

    @GetMapping("/genres")
    List<Mpa> findAllGenres() {
        return service.getAllGenres();
    }

    @GetMapping("/mpa")
    List<Mpa> findAllCategories() {
        return service.getAllCategories();
    }


    @GetMapping("/genres/{id}")
    Mpa findGenreById(@PathVariable int id) {
        return service.getGenre(id);
    }

    @GetMapping("/mpa/{id}")
    Mpa findCategoryById(@PathVariable int id) {
        return service.getCategory(id);
    }
}
