package com.quiz.javaquizapi.service;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.model.BaseEntity;

public abstract class BaseUpdatableService<E extends BaseEntity> extends BaseQuizService<E> implements Updatable<E> {
    public BaseUpdatableService(BaseRepository<E> repository) {
        super(repository);
    }

    @Override
    public void create(E entity) {
        throw new UnsupportedOperationException("Update operations available only.");
    }
}
