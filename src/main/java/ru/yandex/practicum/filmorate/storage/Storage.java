package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {
    int generateId();
    List<T> getAll();
    boolean exist(int id);
    void add(T entity);
    void update(T entity);
    T getById(int id);
}
