package com.quiz.javaquizapi.facade.mapping;

import com.quiz.javaquizapi.model.BaseEntity;

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
     *           <p>Can be either {@link BaseEntity}
     *           <p>or {@link com.quiz.javaquizapi.dto.BaseDto}.
     * @return converted object.
     */
    <D> D map(Object source, Class<D> destinationType);

    /**
     * Transfers provided source object properties to the specified destination object.
     *
     * @param source source object.
     * @param destination destination object.
     */
    void map(Object source, Object destination);
}
