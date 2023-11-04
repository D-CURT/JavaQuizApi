package com.quiz.javaquizapi.dto.personal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.quiz.javaquizapi.annotation.Me;
import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.model.profile.personal.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Me(Address.class)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressDto extends BaseDto {
    private String street;
    private String postalCode;
    private String city;
    private String region;
    private String country;
    private String infoCode;
}
