package ru.yandex.practicum.filmorate.service.DataBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.database.AdditionalDao;

import java.util.List;

@Service
public class AdditionalService {

    private final AdditionalDao dao;

    @Autowired
    public AdditionalService(AdditionalDao dao) {this.dao = dao;}

    public List<Mpa> getAllGenres() {
        return dao.getAllGenres();
    }

    public List<Mpa> getAllCategories() {
        return dao.getAllCategories();
    }

    public Mpa getGenre(int id) {
        if (dao.getGenre(id).isPresent()) {
            return dao.getGenre(id).get();
        } else {
            return null;
        }
    }

    public Mpa getCategory(int id) {
        if (dao.getCategory(id).isPresent()) {
            return dao.getCategory(id).get();
        } else {
            return null;
        }
    }
}
