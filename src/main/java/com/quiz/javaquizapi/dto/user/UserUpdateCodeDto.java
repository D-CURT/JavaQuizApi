package com.quiz.javaquizapi.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.annotation.Me;
import com.quiz.javaquizapi.dto.MeDto;
import com.quiz.javaquizapi.model.user.UserUpdateCode;
import com.quiz.javaquizapi.model.user.UserUpdateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Me(UserUpdateCode.class)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateCodeDto extends MeDto {
    @NotBlank(groups = UserUpdate.class, message = "api.errorCode.40")
    @Size(min = 4, max = 4, groups = UserUpdate.class, message = "api.errorCode.41")
    private String checkNumber;
    @NotBlank(groups = UserUpdate.class, message = "api.errorCode.40")
    private String value;
    @NotNull(groups = UserUpdate.class, message = "api.errorCode.40")
    private UserUpdateType type;

    @Override
    public UserUpdateCodeDto nullify() {
        super.nullify();
        this.value = null;
        return this;
    }

    public interface UserUpdate {
    }
}
