package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {

    List<T> getAll();

    int add(T entity);

    void update(T entity);

    Optional<T> getById(int id);
}
