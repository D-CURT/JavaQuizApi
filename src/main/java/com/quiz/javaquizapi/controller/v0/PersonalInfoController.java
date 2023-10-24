package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.controller.BaseMeController;
import com.quiz.javaquizapi.dto.PersonalInfoDto;
import com.quiz.javaquizapi.facade.me.profile.personal.PersonalInfoFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile/info")
public class PersonalInfoController extends BaseMeController<PersonalInfo, PersonalInfoDto> {
    public PersonalInfoController(ResponseService responseService, PersonalInfoFacade facade) {
        super(responseService, facade);
    }

    @PostMapping(value = StringUtils.EMPTY, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response create() {
        return create(createMeDto(PersonalInfoDto.class));
    }
}
