package com.quiz.javaquizapi.service.me;

import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.service.QuizService;

/**
 * Provides all functionality related to the current user.
 *
 * @param <E> entity type.
 */
public interface MeService<E extends BaseEntity> extends QuizService<E> {
    /**
     * Fetches a generic entity by username of the current user.
     *
     * @param username the current username
     * @return entity of {@link E} type.
     */
    E getMe(String username);
}
