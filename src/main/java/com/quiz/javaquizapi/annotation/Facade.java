package com.quiz.javaquizapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * Indicates that the annotated class is a <strong>Facade</strong>.
 * Provides Spring {@link Component} annotation functionality.
 *
 * <p>Facades accumulate all intermediary handling logic between <strong>Controller</strong>
 * and <strong>Service</strong> layers.
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Facade {
}
