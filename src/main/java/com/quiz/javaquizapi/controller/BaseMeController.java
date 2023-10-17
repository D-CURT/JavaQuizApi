package com.quiz.javaquizapi.controller;

import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.facade.me.MeFacade;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Provides basic functionality of all controllers that operate with data related to the current user.
 *
 * @param <E> entity type.
 * @param <D> data transfer type.
 */
public abstract class BaseMeController<E extends BaseEntity, D extends BaseDto> extends BaseController<D> {
    public BaseMeController(ResponseService responseService, MeFacade<E, D> facade) {
        super(responseService, facade);
    }

    /**
     * Fetches an entity related to the current username.
     *
     * @return fetched data of {@link D} type.
     */
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getMe() {
        return getResponseService().build(getFacade().getMe(getCurrentUsername()));
    }

    @Override
    public MeFacade<E, D> getFacade() {
        return (MeFacade<E, D>) super.getFacade();
    }
}
