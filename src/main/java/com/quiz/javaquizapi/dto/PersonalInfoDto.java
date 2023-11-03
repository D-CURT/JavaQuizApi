package com.quiz.javaquizapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.annotation.Me;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Me(PersonalInfo.class)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonalInfoDto extends MeDto {
    private String bio;
    private String profileCode;
}
