package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.UserFacade;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.user.QuizUserDetails;
import com.quiz.javaquizapi.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides all endpoints linked to the <strong>User</strong>.
 * <p>Includes <strong>home page</strong>, <strong>login</strong>, <strong>logout</strong>
 * and <strong>authorization</strong>.
 * <p>Version: <strong>V0</strong>.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserControllerV0 {

    private final UserFacade facade;
    private final ResponseService responseService;

    /**
     * Users will be redirected to this endpoint after successful authentication.
     *
     * @return greetings to the authenticated user
     */
    @Operation(summary = "Page in progress")
    @GetMapping(StringUtils.EMPTY)
    public String userHome() {
        // TODO implement authenticated user home page
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object name = null;
        if (principal instanceof DefaultOidcUser oidcUser) {
            name = oidcUser.getAttribute("name");
        } else if (principal instanceof QuizUserDetails quizUser) {
            name = quizUser.getUsername();
        }
        return "Welcome '%s' to Java Quiz API!".formatted(name);
    }

    /**
     * Authorizes a user with accepted details.
     *
     * @param data details of the user to authorize
     * @return Response with authorized user
     */
    @Operation(summary = "Authorizes a new user in the system",
            method = "POST",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                            description = "User authorization succeeded"),
                    // TODO provide correct response example
                    @ApiResponse(
                            responseCode = "409",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                            description = "User already exists"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "This status is not supported")})
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(value = "/authorization", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response authorize(@RequestBody @Validated(UserDto.Authorization.class) UserDto data) {
        facade.authorize(data);
        return responseService.build(data);
    }
}
