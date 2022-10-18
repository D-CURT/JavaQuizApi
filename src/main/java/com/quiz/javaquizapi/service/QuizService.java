package com.quiz.javaquizapi.service;

import com.quiz.javaquizapi.model.BaseEntity;

/**
 * Provides all entity related functionality.
 *
 * @param <E> entity type.
 */
public interface QuizService<E extends BaseEntity> {
    /**
     * Creates a new entity.
     *
     * @param entity entity to save in the system.
     */
    void create(E entity);

    /**
     * @return entity type.
     */
    Class<E> getEntityType();
}
