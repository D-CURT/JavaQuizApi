package com.quiz.javaquizapi.facade;

/**
 * Converts JPA entities into DTOs and back.
 */
public interface Mapper {
    /**
     * Maps provided source object into the specified destination type.
     *
     * @param source source object.
     * @param destinationType destination {@link Class<D>} type.
     * @param <D> destination generic type.
     *           <p>Can be either {@link com.quiz.javaquizapi.model.AbstractEntity}
     *           <p>or {@link com.quiz.javaquizapi.dto.BaseDto}.
     * @return converted object.
     */
    <D> D map(Object source, Class<D> destinationType);
}
