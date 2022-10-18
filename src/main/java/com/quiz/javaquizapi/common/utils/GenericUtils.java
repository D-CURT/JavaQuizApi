package com.quiz.javaquizapi.common.utils;

import java.lang.reflect.ParameterizedType;

/**
 * Provides utility methods to operate with generics.
 */
public final class GenericUtils {

    /**
     * Search for the first generic type of the accepted object type.
     *
     * @param type object type to find a generic in it.
     * @param <T> object type.
     * @param <G> generic type.
     * @return the first generic of {@link G} type
     */
    public static <T, G> Class<G> findFirstGeneric(Class<T> type) {
        return findGeneric(type, 0);
    }

    /**
     * Search for the second generic type of the accepted object type.
     *
     * @param type object type to find a generic in it.
     * @param <T> object type.
     * @param <G> generic type.
     * @return the second generic of {@link G} type
     */
    public static <T, G> Class<G> findSecondGeneric(Class<T> type) {
        return findGeneric(type, 1);
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
}
