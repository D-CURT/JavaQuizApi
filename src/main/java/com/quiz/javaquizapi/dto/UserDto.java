package com.quiz.javaquizapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto extends BaseDto {

    /**
     * The essential field of a <strong>User</strong> detail. <strong>Cannot be null or empty</strong>.
     */
    @NotBlank(groups = Authorization.class, message = "api.errorCode.40")
    @Size(min = 8, max = 100, groups = Authorization.class, message = "api.errorCode.41")
    @Email(regexp = "^[^0-9]*[0-9][^0-9]*[A-Za-z]*" +
            "[~`! @#$%^&*()_\\-+\\\\=\\{\\[\\}\\]|\\:;\\\"'<,>.?\\/]{1}@[A-Za-z0-9]+[.]{1}[a-z]{2,}$",
            groups = Authorization.class, message = "api.errorCode.43")
    private String username;
    @NotBlank(groups = Authorization.class, message = "api.errorCode.40")
    @Size(min = 1, max = 50, groups = Authorization.class, message = "api.errorCode.41")
    private String password;
    private String displayName;

    public interface Authorization {
    }
}
