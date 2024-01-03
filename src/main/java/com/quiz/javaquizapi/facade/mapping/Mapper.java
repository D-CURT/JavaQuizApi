package com.quiz.javaquizapi.facade.mapping;

import com.quiz.javaquizapi.model.BaseEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts JPA entities into DTOs and back.
 */
public interface Mapper {
    /**
     * Maps provided source object into the specified destination type.
     *
     * @param source source object.
     * @param destinationType destination {@link Class<D>} type.
     * @param <D> destination generic type.
     *           <p>Can be either {@link BaseEntity}
     *           <p>or {@link com.quiz.javaquizapi.dto.BaseDto}.
     * @return converted object.
     */
    <D> D map(Object source, Class<D> destinationType);

    /**
     * Transfers provided source object properties to the specified destination object.
     *
     * @param source source object.
     * @param destination destination object.
     */
    void map(Object source, Object destination);

    <S, D> Page<D> mapPage(Page<S> page, Class<D> target);

    /**
     * Does lists conversion.
     * @param source source list type.
     * @param targetClass target list type.
     * @param <S> source object type.
     * @param <T> target object type.
     * @return converted list.
     */
    default <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> map(element, targetClass))
                .collect(Collectors.toList());
    }
}
