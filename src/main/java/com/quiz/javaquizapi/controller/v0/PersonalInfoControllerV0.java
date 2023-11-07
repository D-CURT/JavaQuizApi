package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.controller.BaseMeController;
import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.dto.personal.AddressDto;
import com.quiz.javaquizapi.dto.personal.ContactDto;
import com.quiz.javaquizapi.dto.personal.PersonalInfoDto;
import com.quiz.javaquizapi.dto.personal.SocialMediaDto;
import com.quiz.javaquizapi.facade.me.profile.personal.PersonalInfoFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

@RestController
@RequestMapping("/profile/info")
public class PersonalInfoControllerV0 extends BaseMeController<PersonalInfo, PersonalInfoDto> {
    public PersonalInfoControllerV0(ResponseService responseService, PersonalInfoFacade facade) {
        super(responseService, facade);
    }

    @GetMapping("/full")
    public Response getFull(@RequestParam(name = "profile-code") String profileCode) {
        return getResponseService().build(cast(getFacade(), PersonalInfoFacade.class).getFull(profileCode));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = StringUtils.EMPTY, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response create() {
        return create(createMeDto(PersonalInfoDto.class));
    }

    @PostMapping(value = StringUtils.EMPTY, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response update(@RequestBody PersonalInfoDto data) {
        data.setUsername(getCurrentUsername());
        cast(getFacade(), PersonalInfoFacade.class).updateMe(data);
        return getResponseService().ok();
    }

    @PutMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createAddress(@RequestBody @Validated(BaseDto.Create.class) AddressDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateAddress(data);
        return getResponseService().ok();
    }

    @PostMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateAddress(@RequestBody @Validated(BaseDto.Update.class) AddressDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateAddress(data);
        return getResponseService().ok();
    }

    @PutMapping(value = "/contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createContact(@RequestBody @Validated(BaseDto.Create.class) ContactDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateContact(data);
        return getResponseService().ok();
    }

    @PostMapping(value = "/contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateContact(@RequestBody @Validated(BaseDto.Update.class) ContactDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateContact(data);
        return getResponseService().ok();
    }

    @PutMapping(value = "/media", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createMedia(@RequestBody @Validated(BaseDto.Create.class) SocialMediaDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateSocialMedia(data);
        return getResponseService().ok();
    }

    @PostMapping(value = "/media", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateMedia(@RequestBody @Validated(BaseDto.Update.class) SocialMediaDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateSocialMedia(data);
        return getResponseService().ok();
    }
}
