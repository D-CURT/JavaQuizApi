package com.quiz.javaquizapi.service;

import com.quiz.javaquizapi.common.utils.GenericUtils;
import com.quiz.javaquizapi.common.utils.ValidationUtils;
import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.model.BaseEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides basic functionality of {@link QuizService}.
 *
 * @param <E> entity type.
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class BaseQuizService<E extends BaseEntity> implements QuizService<E> {
    public static final String CODE_ERROR_FORMAT = "Unable to create one %s: provided code is malformed, check its format - UUID is required.";
    protected static final String ENTITY_IDENTIFIER = "code";
    private final Class<E> entityType = GenericUtils.findFirstGeneric(getClass());
    private final BaseRepository<E> repository;

    protected void logFetchingEntity() {
        logFetchingByField(ENTITY_IDENTIFIER);
    }

    protected void logFetchingByField(String fieldName) {
        log.info("Fetching a {} by {}...", getEntityType().getSimpleName(), fieldName);
    }

    protected void setCodeIfValid(E entity) {
        setCodeIfValid(entity, CODE_ERROR_FORMAT.formatted(entity.getClass().getSimpleName()));
    }

    protected void setCodeIfValid(E entity, String errorMessage) {
        log.info("Resolving a new entity unique code...");
        Optional.ofNullable(entity.getCode())
                .filter(StringUtils::isNotBlank)
                .ifPresentOrElse(
                        value -> entity.setCode(ValidationUtils.validator(errorMessage).validateCode(value)),
                        () -> entity.setCode(UUID.randomUUID().toString()));
    }
}
