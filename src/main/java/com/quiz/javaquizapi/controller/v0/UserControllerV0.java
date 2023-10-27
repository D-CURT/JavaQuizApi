package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.controller.BaseMeController;
import com.quiz.javaquizapi.dto.user.UserDto;
import com.quiz.javaquizapi.dto.user.PasswordCodeDto;
import com.quiz.javaquizapi.facade.me.user.UserFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides all endpoints linked to the <strong>User</strong>.
 * <p>Includes <strong>authorization</strong> and <strong>logout</strong>.
 * <p>Version: <strong>V0</strong>.
 */
@RestController
@RequestMapping("/user")
public class UserControllerV0 extends BaseMeController<User, UserDto> {
    public UserControllerV0(ResponseService responseService, UserFacade facade) {
        super(responseService, facade);
    }

    /**
     * Authorizes a user with accepted details.
     *
     * @param data details of the user to authorize.
     * @return Response with authorized user.
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/authorization", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response authorize(@RequestBody @Validated(UserDto.Authorization.class) UserDto data) {
        return create(data);
    }

    /**
     * Sends a password update email to the current username.
     *
     * @return empty response.
     */
    @PostMapping("/me/password/code")
    public Response sendPasswordCode() {
        castFacade(UserFacade.class).sendCodeToChangePassword(getCurrentUsername());
        return getResponseService().ok();
    }

    @PostMapping("/me/password/update")
    public Response changePassword(@RequestBody @Validated(PasswordCodeDto.PasswordChange.class) PasswordCodeDto data) {
        data.setUsername(getCurrentUsername());
        castFacade(UserFacade.class).changePassword(data);
        return getResponseService().ok();
    }

    //TODO implement user archive
}
