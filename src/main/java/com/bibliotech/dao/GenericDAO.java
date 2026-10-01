package com.bibliotech.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    void save(T entity);

    void update(ID id, T entity);

    void delete(ID id);
}