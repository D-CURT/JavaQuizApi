package com.quiz.javaquizapi.controller.v0.profile;

import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.dto.personal.AddressDto;
import com.quiz.javaquizapi.dto.personal.ContactDto;
import com.quiz.javaquizapi.dto.personal.PersonalInfoDto;
import com.quiz.javaquizapi.dto.personal.SocialMediaDto;
import com.quiz.javaquizapi.facade.me.profile.personal.PersonalInfoFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

@RestController
public class PersonalInfoControllerV0 extends BaseProfileController<PersonalInfo, PersonalInfoDto> {
    public PersonalInfoControllerV0(ResponseService responseService, PersonalInfoFacade facade) {
        super(responseService, facade);
    }

    @Override
    @GetMapping("/me/info")
    public Response getMe() {
        return super.getMe();
    }

    @GetMapping("/{profile-code}/info")
    public Response getFull(@PathVariable(name = "profile-code") String profileCode) {
        return getResponseService().build(cast(getFacade(), PersonalInfoFacade.class).getFull(profileCode));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/me/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response create() {
        return create(createMeDto(PersonalInfoDto.class));
    }

    @PostMapping(value = "/me/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response update(@RequestBody PersonalInfoDto data) {
        data.setUsername(getCurrentUsername());
        cast(getFacade(), PersonalInfoFacade.class).updateMe(data);
        return getResponseService().ok();
    }

    @PutMapping(value = "/me/info/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createAddress(@RequestBody @Validated(BaseDto.Create.class) AddressDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateAddress(data);
        return getResponseService().ok();
    }

    @PostMapping(value = "/me/info/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateAddress(@RequestBody @Validated(BaseDto.Update.class) AddressDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateAddress(data);
        return getResponseService().ok();
    }

    @PutMapping(value = "/me/info/contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createContact(@RequestBody @Validated(BaseDto.Create.class) ContactDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateContact(data);
        return getResponseService().ok();
    }

    @PostMapping(value = "/me/info/contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateContact(@RequestBody @Validated(BaseDto.Update.class) ContactDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateContact(data);
        return getResponseService().ok();
    }

    @PutMapping(value = "/me/info/media", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createMedia(@RequestBody @Validated(BaseDto.Create.class) SocialMediaDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateSocialMedia(data);
        return getResponseService().ok();
    }

    @PostMapping(value = "/me/info/media", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateMedia(@RequestBody @Validated(BaseDto.Update.class) SocialMediaDto data) {
        cast(getFacade(), PersonalInfoFacade.class).updateSocialMedia(data);
        return getResponseService().ok();
    }
}
