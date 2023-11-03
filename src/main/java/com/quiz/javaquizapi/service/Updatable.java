package com.quiz.javaquizapi.service;

/**
 * Provides functionality to update objects.
 * @param <T> updatable object type.
 */
public interface Updatable<T> {
    void update(T object);
}
