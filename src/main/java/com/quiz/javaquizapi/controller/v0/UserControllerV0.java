package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.controller.BaseMeController;
import com.quiz.javaquizapi.dto.RestorePasswordDto;
import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.me.user.UserFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * @param data details of the user to authorize
     * @return Response with authorized user
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/authorization", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response authorize(@RequestBody @Validated(UserDto.Authorization.class) UserDto data) {
        return create(data);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/password_code")
    public Response sendPasswordCode() {
        ((UserFacade) getFacade()).sendPasswordCode(getCurrentUsername());
        return getResponseService().build(null);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/restore_password", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response restorePassword(@RequestBody @Validated(RestorePasswordDto.RestorePassword.class) RestorePasswordDto data) {
        UserDto userDto = ((UserFacade) getFacade()).restorePassword(getCurrentUsername(), data);
        return getResponseService().build(userDto);
    }
}
