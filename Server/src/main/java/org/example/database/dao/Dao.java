package org.example.database.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();
    void create(T object);
    void update(T object);
    void remove(T object);
}
