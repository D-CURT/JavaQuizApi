package com.quiz.javaquizapi.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains common fields for all of the DTOs.
 */
@Getter
@Setter
public abstract class BaseDto implements Serializable {
    @ApiModelProperty(
            example = "1d7aefe9-110d-48e4-a8dc-b02f4258eca2",
            notes = "Value is unique. Use this one to find an entity in our database. " +
                    "If a value is provided - it will be saved in the database while entity creation, " +
                    "otherwise a new value will be generated and then show in a response, " +
                    "if an entity creation succeeded.")
    public String code;
}
