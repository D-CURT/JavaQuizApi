package com.quiz.javaquizapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Contains common fields for all Me DTOs.
 */
@Getter
@Setter
@Accessors
public abstract class MeDto extends BaseDto {
    /**
     * The essential field of a <strong>User</strong> detail. <strong>Cannot be null or empty</strong>.
     */
    @NotBlank(groups = UserDto.Authorization.class, message = "api.errorCode.40")
    @Size(min = 1, max = 50, groups = UserDto.Authorization.class, message = "api.errorCode.41")
    @Pattern(regexp = "^[\\w\\-_]+$", message = "api.errorCode.43")
    private String username;

    /**
     * Removes username value.
     * @return this.
     */
    public MeDto nullify() {
        username = null;
        return this;
    }
}
