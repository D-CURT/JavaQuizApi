package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.controller.BaseMeController;
import com.quiz.javaquizapi.dto.ProfileDto;
import com.quiz.javaquizapi.facade.me.profile.ProfileFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides all endpoints linked to the <strong>Profile</strong>.
 * <p>Version: <strong>V0</strong>.
 */
@RestController
@RequestMapping("/profile")
public class ProfileControllerV0 extends BaseMeController<Profile, ProfileDto> {
    public ProfileControllerV0(ResponseService responseService, ProfileFacade facade) {
        super(responseService, facade);
    }

    /**
     * Creates and empty profile with default stats.
     * <p>Profile is linked to the current user.
     *
     * @return a new {@link Profile}.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = StringUtils.EMPTY, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response create() {
        return create(createMeDto(ProfileDto.class));
    }
}
