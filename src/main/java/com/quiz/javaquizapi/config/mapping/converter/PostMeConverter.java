package com.quiz.javaquizapi.config.mapping.converter;

import com.quiz.javaquizapi.dto.MeDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * Provides Me dtos post conversion.
 * @param <S> source type.
 * @param <D> destination type {@link MeDto}.
 */
public class PostMeConverter<S, D extends MeDto> implements Converter<S, D> {
    @Override
    public D convert(MappingContext<S, D> mappingContext) {
        D destination = mappingContext.getDestination();
        destination.nullify();
        return destination;
    }
}
