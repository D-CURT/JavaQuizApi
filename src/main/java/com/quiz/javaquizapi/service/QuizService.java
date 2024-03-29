package com.quiz.javaquizapi.service;

import com.quiz.javaquizapi.model.BaseEntity;

/**
 * Provides all entity related functionality.
 *
 * @param <E> entity type.
 */
public interface QuizService<E extends BaseEntity> {
    /**
     * Fetches a generic entity its code.
     *
     * @param code the entity code.
     * @return requested entity.
     */
    E get(String code);

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
