package com.quiz.javaquizapi.service.me;

import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.service.BaseQuizService;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * Provides basic functionality of {@link MeService}.
 *
 * @param <E> entity type.
 */
@Slf4j
public abstract class BaseMeService<E extends BaseEntity> extends BaseQuizService<E> implements MeService<E> {
    protected static final String USER_IDENTIFIER = "username";
    @Override
    protected void logFetchingEntity() {
        logFetchingByField(USER_IDENTIFIER);
    }
}
