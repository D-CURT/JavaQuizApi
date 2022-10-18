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
    protected E getMe(String username, Function<String, E> producer) {
        log.info("Fetching a {} by username...", getEntityType().getSimpleName());
        return producer.apply(username);
    }
}
