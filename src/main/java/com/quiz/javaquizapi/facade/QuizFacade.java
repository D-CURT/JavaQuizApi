package com.quiz.javaquizapi.facade;

import com.quiz.javaquizapi.dto.BaseDto;

/**
 * Provides intermediary operations related to a {@link D} data type.
 *
 * @param <D> related dto type
 */
public interface QuizFacade <D extends BaseDto> {
    /**
     * Creates an entity in the system.
     *
     * @param dto data to handle
     */
    void create(D dto);
}
