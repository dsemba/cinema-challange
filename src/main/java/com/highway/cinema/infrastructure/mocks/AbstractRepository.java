package com.highway.cinema.infrastructure.mocks;

import com.highway.cinema.domain.dao.Repository;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T> implements Repository<T> {
    protected List<T> entities = new ArrayList<>();

    @Override
    public List<T> findAll() {
        return entities;
    }

    @Override
    public T save(T entity) {
        entities.add(entity);
        return entity;
    }
}
