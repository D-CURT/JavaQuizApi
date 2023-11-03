package com.quiz.javaquizapi.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.annotation.Me;
import com.quiz.javaquizapi.dto.MeDto;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Contains a <strong>User</strong> details.
 */
@Getter
@Setter
@Me(User.class)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto extends MeDto {
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    @Size(min = 1, max = 50, groups = Create.class, message = "api.errorCode.41")
    private String password;
    private String displayName;
    private Boolean enabled;
    @Null(groups = Authorization.class, message = "api.errorCode.44")
    private Roles role;

    @Override
    public UserDto nullify() {
        super.nullify();
        password = null;
        enabled = null;
        return this;
    }

    public interface Authorization {

    }
}
