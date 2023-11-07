package com.quiz.javaquizapi.config.mapping.configurer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface MappingConfigurer {
    /**
     * Applies a mapper configurations.
     * @param mapper accepted mapper.
     */
    void configure(ModelMapper mapper);

    /**
     * Provides TypeMap aggregation functions.
     * @implNote the class was created for the sake of increasing readability of basic operation with TypeMap.
     *
     * @param <S> source type, extends {@link com.quiz.javaquizapi.model.BaseEntity}.
     * @param <D> destination type, extends {@link com.quiz.javaquizapi.dto.BaseDto}.
     */
    @Accessors(chain = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class TypeMapAggregator<S, D> {
        private final Class<D> destination;
        private final Class<S> source;
        @Setter(AccessLevel.PRIVATE)
        private ModelMapper mapper;

        static <S, D> TypeMapAggregator<S, D> of(Class<S> source, Class<D> destination) {
            return new TypeMapAggregator<>(destination, source);
        }

        /**
         * Incorporates ModelMapper for future TypeMap aggregation.
         * @param mapper Model mapper object.
         * @return this aggregator.
         */
        TypeMapAggregator<S, D> apply(ModelMapper mapper) {
            return setMapper(mapper);
        }

        /**
         * Creates a TypeMap, or uses one that already created.
         * <p>Aggregates accepted consumer for the TypeMap.
         * @param consumer function to configure a TypeMap.
         */
        void aggregate(Consumer<TypeMap<S, D>> consumer) {
            Optional.ofNullable(mapper.getTypeMap(source, destination))
                    .or(() -> Optional.of(mapper.createTypeMap(source, destination)))
                    .ifPresent(consumer);
        }

        /**
         * Applies an accepted post converter.
         * @implNote  <strong>aggregate</strong> method docs.
         * @param supplier post converter builder.
         * @param <T> converter type.
         */
        <T extends Converter<S, D>> void postConvert(Supplier<T> supplier) {
            aggregate(mapping -> mapping.setPostConverter(supplier.get()));
        }
    }
}
