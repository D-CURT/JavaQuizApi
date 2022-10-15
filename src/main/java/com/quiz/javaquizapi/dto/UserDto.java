package com.quiz.javaquizapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Contains a <strong>User</strong> details.
 */
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class UserDto extends BaseDto {
    /**
     * The essential field of a <strong>User</strong> detail. <strong>Cannot be null or empty</strong>.
     */
    @ApiModelProperty(example = "User1", required = true)
    @NotBlank(groups = Authorization.class, message = "api.errorCode.40")
    @Size(min = 1, max = 50, groups = Authorization.class, message = "api.errorCode.41")
    private final String username;
    @ApiModelProperty(example = "UserPassword1", required = true)
    @NotBlank(groups = Authorization.class, message = "api.errorCode.40")
    @Size(min = 1, max = 50, groups = Authorization.class, message = "api.errorCode.41")
    private String password;

    public interface Authorization {}
}
