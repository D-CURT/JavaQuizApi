package com.quiz.javaquizapi.controller;

import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.facade.QuizFacade;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.model.user.QuizUserDetails;
import com.quiz.javaquizapi.service.response.ResponseService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

/**
 * Provides basic functionality of all controllers.
 *
 * @param <E> entity type.
 * @param <D> data transfer type.
 */
@Getter
@RequiredArgsConstructor
public abstract class BaseController<E extends BaseEntity, D extends BaseDto> {
    private final ResponseService responseService;
    private final QuizFacade<E, D> facade;

    /**
     * Requests an entity by its code.
     *
     * @param code entity code.
     * @return a {@link Response} with an object of {@link D} type in it.
     */
    public Response get(String code) {
        return getResponseService().build(getFacade().get(code));
    }

    /**
     * Creates an entity withing the system and returns it then.
     *
     * @param data data transfer type.
     * @return a {@link Response} with an object of {@link D} type in it.
     */
    public Response create(D data) {
        facade.create(data);
        return responseService.build(data);
    }

    /**
     * Fetches a username of the currently authenticated user.
     *
     * @return the current username.
     */
    protected String getCurrentUsername() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof DefaultOidcUser oidcUser) {
            username = oidcUser.getEmail();
        } else if (principal instanceof QuizUserDetails quizUser) {
            username = quizUser.getUsername();
        }
        return username;
    }
}
