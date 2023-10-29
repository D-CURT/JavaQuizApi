package com.quiz.javaquizapi.facade.me;

import com.quiz.javaquizapi.common.utils.GenericUtils;
import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.facade.BaseQuizFacade;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.service.me.MeService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Provides basic functionality of {@link MeFacade}.
 *
 * @param <E> entity type.
 * @param <D> data type.
 */
@RequiredArgsConstructor
public abstract class BaseMeFacade<E extends BaseEntity, D extends BaseDto> extends BaseQuizFacade<D>
        implements MeFacade<E, D> {
    @Getter(AccessLevel.PROTECTED)
    private final Class<E> entityType = GenericUtils.findFirstGeneric(getClass());
    protected final MeService<E> service;
    protected final Mapper mapper;

    @Override
    public D getMe(String username) {
        return mapper.map(service.getMe(username), getDataType());
    }
}
