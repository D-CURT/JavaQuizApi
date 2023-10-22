package com.quiz.javaquizapi.service;

import com.quiz.javaquizapi.common.utils.GenericUtils;
import com.quiz.javaquizapi.exception.ValidationException;
import com.quiz.javaquizapi.model.BaseEntity;
import lombok.Getter;
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
public abstract class BaseQuizService<E extends BaseEntity> implements QuizService<E> {
    public static final String CODE_ERROR_FORMAT = "Unable to create one %s: provided code is malformed, check its format - UUID is required. ";
    private final Class<E> entityType = GenericUtils.findFirstGeneric(getClass());

    protected void setCodeIfValid(E entity) {
        setCodeIfValid(entity, CODE_ERROR_FORMAT.formatted(entity.getClass().getSimpleName()));
    }

    protected void setCodeIfValid(E entity, String errorMessage) {
        log.info("Resolving a new entity unique code...");
        Optional.ofNullable(entity.getCode())
                .filter(StringUtils::isNotBlank)
                .ifPresentOrElse(
                        value -> {
                            try {
                                entity.setCode(UUID.fromString(value).toString());
                            } catch (Exception e) {
                                throw ValidationException.getCodeMalformedException(errorMessage, value);
                            }
                        },
                        () -> entity.setCode(UUID.randomUUID().toString()));
    }
}
