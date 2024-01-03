package com.quiz.javaquizapi.facade.mapping;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

/**
 * Converts JPA entities into DTOs and back.
 */
public class QuizModelMapper extends ModelMapper implements Mapper {
    @Override
    public <S, D> Page<D> mapPage(Page<S> page, Class<D> target) {
        return page.map(entity -> map(entity, target));
    }
}
