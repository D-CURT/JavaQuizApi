package com.quiz.javaquizapi.facade;

import com.quiz.javaquizapi.common.util.GenericUtils;
import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.service.QuizService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Provides basic functionality of {@link QuizFacade}.
 *
 * @param <D> data type.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter(AccessLevel.PROTECTED)
public abstract class BaseQuizFacade<E extends BaseEntity, D extends BaseDto> implements QuizFacade<E, D> {
    private final Class<E> entityType = GenericUtils.findFirstGeneric(getClass());
    private final Class<D> dataType = GenericUtils.findSecondGeneric(getClass());
    private final QuizService<E> service;
    private final Mapper mapper;

    @Override
    public D get(String code) {
        return mapper.map(service.get(code), dataType());
    }
}
