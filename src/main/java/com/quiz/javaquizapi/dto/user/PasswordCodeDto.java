package com.quiz.javaquizapi.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.annotation.Me;
import com.quiz.javaquizapi.dto.MeDto;
import com.quiz.javaquizapi.model.user.PasswordCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Me(PasswordCode.class)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PasswordCodeDto extends MeDto {
    @NotBlank(groups = PasswordChange.class, message = "api.errorCode.40")
    @Size(min = 4, max = 4, groups = PasswordChange.class, message = "api.errorCode.41")
    private String checkNumber;
    @NotBlank(groups = PasswordChange.class, message = "api.errorCode.40")
    private String password;

    public interface PasswordChange {
    }
}
