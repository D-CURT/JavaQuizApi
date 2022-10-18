package com.quiz.javaquizapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.model.profile.Tiers;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Contains a <strong>Profile</strong> details.
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileDto extends BaseDto {
    private Tiers tier;
    private String userCode;
}
