package com.quiz.javaquizapi.dto.personal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressDto extends BaseDto {
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    private String street;
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    private String postalCode;
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    private String city;
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    private String region;
    @NotBlank(groups = Create.class, message = "api.errorCode.40")
    private String country;
    @NotBlank(groups = {Create.class, Update.class}, message = "api.errorCode.40")
    private String infoCode;
    private String formattedAddress;
}
