package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.controller.BaseMeController;
import com.quiz.javaquizapi.dto.PersonalInfoDto;
import com.quiz.javaquizapi.facade.me.profile.personal.PersonalInfoFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile/info")
public class PersonalInfoControllerV0 extends BaseMeController<PersonalInfo, PersonalInfoDto> {
    public PersonalInfoControllerV0(ResponseService responseService, PersonalInfoFacade facade) {
        super(responseService, facade);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = StringUtils.EMPTY, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response create() {
        return create(createMeDto(PersonalInfoDto.class));
    }
}
