package com.quiz.javaquizapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RestorePasswordDto {

    @NotBlank(groups = RestorePasswordDto.class, message = "api.errorCode.40")
    @Size(min = 8, max = 100, groups = RestorePasswordDto.class, message = "api.errorCode.41")
    @Email(regexp = "^[^0-9]*[0-9][^0-9]*[A-Za-z]*" +
            "[~`! @#$%^&*()_\\-+\\\\=\\{\\[\\}\\]|\\:;\\\"'<,>.?\\/]{1}@[A-Za-z0-9]+[.]{1}[a-z]{2,}$",
            groups = RestorePasswordDto.class, message = "api.errorCode.43")
    private String newPass;

    @NotBlank(groups = RestorePasswordDto.class, message = "api.errorCode.44")
    private String code;

    public interface RestorePassword {

    }
}
