package com.quiz.javaquizapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public abstract class MeDto extends BaseDto implements Nullifiable {
    /**
     * The essential field of a <strong>User</strong> detail. <strong>Cannot be null or empty</strong>.
     */
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    @Size(min = 1, max = 50, groups = Create.class, message = "api.errorCode.41")
    @Email(groups = Create.class, message = "api.errorCode.43")
    private String username;

    /**
     * Removes username value.
     *
     * @return this.
     */
    @Override
    public MeDto nullify() {
        username = null;
        return this;
    }
}
