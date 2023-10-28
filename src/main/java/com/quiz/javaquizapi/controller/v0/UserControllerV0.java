package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.controller.BaseMeController;
import com.quiz.javaquizapi.dto.user.UserDto;
import com.quiz.javaquizapi.dto.user.UserUpdateCodeDto;
import com.quiz.javaquizapi.facade.me.user.UserFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.quiz.javaquizapi.common.utils.GenericUtils.cast;

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
    @PutMapping(value = "/authorization", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response authorize(@RequestBody @Validated(UserDto.Authorization.class) UserDto data) {
        return create(data);
    }

    /**
     * Sends a password update email to the current username.
     *
     * @return empty response.
     */
    @PostMapping("/me/update/code")
    public Response sendPasswordCode() {
        cast(getFacade(), UserFacade.class).sendCodeToChangeUser(getCurrentUsername());
        return getResponseService().ok();
    }

    @PostMapping("/me/update")
    public Response update(@RequestBody @Validated(UserUpdateCodeDto.UserUpdate.class) UserUpdateCodeDto data) {
        data.setUsername(getCurrentUsername());
        cast(getFacade(), UserFacade.class).updateMe(data);
        return getResponseService().ok();
    }

    @PostMapping("/archive/{code}")
    public Response archive(@PathVariable String code) {
        cast(getFacade(), UserFacade.class).archive(code);
        return getResponseService().ok();
    }
}
