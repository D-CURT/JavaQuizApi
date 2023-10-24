package com.quiz.javaquizapi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Contains common fields for all the DTOs.
 */
@Getter
@Setter
@Accessors
public abstract class BaseDto implements Serializable {
    /**
     * Value is unique. Use this one to find an entity in our database.
     * <p>If a value is provided - it will be saved in the database while entity creation,
     * <p>otherwise a new value will be generated and then show in a response,
     * if an entity creation succeeded.
     */
    public String code;
}
