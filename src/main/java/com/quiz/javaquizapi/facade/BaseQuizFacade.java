package com.quiz.javaquizapi.facade;

import com.quiz.javaquizapi.common.util.GenericUtils;
import com.quiz.javaquizapi.dto.BaseDto;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Provides basic functionality of {@link QuizFacade}.
 *
 * @param <D> data type.
 */
@Getter(AccessLevel.PROTECTED)
public abstract class BaseQuizFacade<D extends BaseDto> implements QuizFacade<D> {
    private final Class<D> dataType = GenericUtils.findSecondGeneric(getClass());
}
