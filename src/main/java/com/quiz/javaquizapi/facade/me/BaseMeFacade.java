package com.quiz.javaquizapi.facade.me;

import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.facade.BaseQuizFacade;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.service.QuizService;
import com.quiz.javaquizapi.service.me.MeService;
import org.modelmapper.TypeToken;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;
import static com.quiz.javaquizapi.common.util.GenericUtils.castMe;

/**
 * Provides basic functionality of {@link MeFacade}.
 *
 * @param <E> entity type.
 * @param <D> data type.
 */
public abstract class BaseMeFacade<E extends BaseEntity, D extends BaseDto> extends BaseQuizFacade<E, D>
        implements MeFacade<E, D> {
    public BaseMeFacade(MeService<E> service, Mapper mapper) {
        super(service, mapper);
    }

    @Override
    public D getMe(String username) {
        return mapper().map(castMe(service()).getMe(username), dataType());
    }

    @Override
    protected MeService<E> service() {
        return castMe(super.service());
    }
}
