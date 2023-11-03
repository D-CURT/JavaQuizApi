package com.quiz.javaquizapi.common.utils;

import com.quiz.javaquizapi.dto.reflection.FieldSetter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

import static org.reflections.ReflectionUtils.getConstructors;

/**
 * Provides reflection utilities, including with usage "org reflections" framework features.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {
    public static final String DEFAULT_PREFIX = "com.quiz.javaquizapi";

    /**
     * The static constructor for the {@link Reflections} that built for the root app package.
     * @return {@link Reflections} object.
     */
    public static Reflections reflections() {
        return reflections(DEFAULT_PREFIX);
    }

    /**
     * The static constructor for the {@link Reflections} that built for the accepted package.
     * @return {@link Reflections} object.
     */
    public static Reflections reflections(String prefix) {
        return new Reflections(String.join(".", DEFAULT_PREFIX, prefix));
    }

    /**
     * Returns provided an annotation of accepted type, if it exists.
     * @param type annotated type.
     * @param annotationType annotation.
     * @param <A> annotations generic type.
     * @return annotation wrapped by Optional.
     */
    public static <A extends Annotation> Optional<A> getAnnotation(Class<?> type, Class<A> annotationType) {
        if (type.isAnnotationPresent(annotationType)) {
            return Optional.of(type.getAnnotation(annotationType));
        }
        return Optional.empty();
    }

    /**
     * Creates an instance of accepted type.
     * @param type object type.
     * @param <T> instance generic.
     * @return an optional instance.
     */
    @SuppressWarnings("all")
    public static <T> Optional<T> getInstance(Class<T> type) {
        return (Optional<T>) getConstructors(type, constructor -> constructor.getParameterCount() == 0)
                .stream()
                .findAny()
                .map(constructor -> {
                    try {
                        return Optional.of(constructor.newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        return Optional.empty();
                    }
                }).orElseGet(Optional::empty);
    }

    /**
     * Sets values by accepted setters.
     * @param values fields setters.
     */
    public static void setValues(FieldSetter<?>... values) {
        Arrays.stream(values).forEach(ReflectionUtils::accept);
    }

    private static <V> void accept(FieldSetter<V> field) {
        field.getSetter().accept(field.getApplicable().getValue());
    }
}
