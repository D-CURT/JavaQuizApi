package com.quiz.javaquizapi.config.mapping.configurer;

import com.quiz.javaquizapi.annotation.Me;
import com.quiz.javaquizapi.common.util.ReflectionUtils;
import com.quiz.javaquizapi.config.mapping.converter.PostMeConverter;
import com.quiz.javaquizapi.dto.MeDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Defines <strong>me</strong> conversion details.
 */
@Component
public class MeMappingConfigurer implements MappingConfigurer {
    /**
     * Resolves <strong>Post</strong> converters for all <strong>Me</strong> dtos.
     * <p>It applies mappings for all pairs of <strong>@Me</strong> dto and linked <strong>@Entity</strong>.
     * @implNote this method uses the "org.reflections" framework to find all the dtos marked with @Me annotation.
     * It applies a generic post converter then, for all dtos that were found.
     */
    @Override
    public void configure(ModelMapper mapper) {
        ReflectionUtils.reflections("dto")
                .getSubTypesOf(MeDto.class)
                .stream()
                .filter(dto -> dto.isAnnotationPresent(Me.class))
                .forEach(dto -> ReflectionUtils.getAnnotation(dto, Me.class)
                        .map(me -> TypeMapAggregator.of(me.value(), dto))
                        .map(aggregator -> aggregator.apply(mapper))
                        .ifPresent(agr -> agr.aggregate(map -> map.setPostConverter(new PostMeConverter<>()))));
    }
}
