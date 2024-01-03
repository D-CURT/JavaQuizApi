package com.quiz.javaquizapi.common.util;

import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.service.QuizService;
import com.quiz.javaquizapi.service.me.MeService;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * Provides utility methods to operate with generics.
 */
public final class GenericUtils {
    /**
     * Search for the first generic type of the accepted object type.
     *
     * @param type object type to find a generic in it.
     * @param <T>  object type.
     * @param <G>  generic type.
     * @return the first generic of {@link G} type
     */
    public static <T, G> Class<G> findFirstGeneric(Class<T> type) {
        return findGeneric(type, 0);
    }

    /**
     * Search for the second generic type of the accepted object type.
     *
     * @param type object type to find a generic in it.
     * @param <T>  object type.
     * @param <G>  generic type.
     * @return the second generic of {@link G} type
     */
    public static <T, G> Class<G> findSecondGeneric(Class<T> type) {
        return findGeneric(type, 1);
    }

    @SuppressWarnings("all")
    public static <T> T cast(Object object, Class<T> type) {
        try {
            return (T) object;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to cast an object", e);
        }
    }

    public static <T extends BaseEntity> MeService<T> castMe(QuizService<T> service) {
        return cast(service, new TypeToken<MeService<T>>() {}.getRawType());
    }

    @SuppressWarnings("all")
    private static <T, G> Class<G> findGeneric(Class<T> type, int index) {
        try {
            return (Class<G>) Class.forName(((ParameterizedType) type.getGenericSuperclass())
                    .getActualTypeArguments()[index].getTypeName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find generic by index", e);
        }
    }

    public static <T> T create(Class<T> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException("Unable to construct an object", e);
        }
    }

    public static <T> Class<Page<T>> getPagedTypeOf(Class<T> type) {
        return new TypeToken<Page<T>>() {}.getRawType();
    }
}
