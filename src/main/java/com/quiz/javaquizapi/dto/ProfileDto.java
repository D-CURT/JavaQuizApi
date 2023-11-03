package com.quiz.javaquizapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.annotation.Me;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.Tiers;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Contains a <strong>Profile</strong> details.
 */
@Getter
@Setter
@Me(Profile.class)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileDto extends MeDto {
    private Tiers tier;
    private long score;
    private long rate;
    private String userCode;
}
