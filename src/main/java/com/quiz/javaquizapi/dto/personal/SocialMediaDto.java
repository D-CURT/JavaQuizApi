package com.quiz.javaquizapi.dto.personal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.model.profile.personal.SocialType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SocialMediaDto extends BaseDto {
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    private String accountName;
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    private String link;
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    private SocialType type;
    @NotBlank(groups = {Create.class, Update.class}, message = "api.errorCode.40")
    private String contactCode;
}
