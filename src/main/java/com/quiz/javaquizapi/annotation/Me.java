package com.quiz.javaquizapi.annotation;

import com.quiz.javaquizapi.model.BaseEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Me {
    Class<? extends BaseEntity> value();
}
