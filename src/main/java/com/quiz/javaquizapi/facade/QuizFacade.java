package com.quiz.javaquizapi.facade;

import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.model.BaseEntity;

/**
 * Provides intermediary operations related to a {@link D} data type.
 *
 * @param <D> related dto type
 */
public interface QuizFacade<E extends BaseEntity, D extends BaseDto> {
    /**
     * Request an entity by its code.
     *
     * @param code entity code.
     * @return received entity.
     */
    D get(String code);

    /**
     * Creates an entity in the system.
     *
     * @param dto data to handle
     */
    void create(D dto);
}
