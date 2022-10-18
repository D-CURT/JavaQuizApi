package com.quiz.javaquizapi.facade.me;

import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.facade.QuizFacade;
import com.quiz.javaquizapi.model.BaseEntity;

/**
 * Provides intermediary operations related to a {@link D} data type linked to the current username.
 *
 * @param <E> entity type.
 * @param <D> data type.
 */
public interface MeFacade<E extends BaseEntity, D extends BaseDto> extends QuizFacade<D> {
    /**
     * Fetches entity by the current username.
     * <p>Converts the result into a proper data object.
     *
     * @param username the current username.
     * @return data of {@link D} type.
     */
    D getMe(String username);
}
