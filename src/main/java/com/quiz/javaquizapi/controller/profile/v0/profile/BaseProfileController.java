package com.quiz.javaquizapi.controller.profile.v0.profile;

import com.quiz.javaquizapi.controller.BaseMeController;
import com.quiz.javaquizapi.dto.BaseDto;
import com.quiz.javaquizapi.facade.me.MeFacade;
import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Provides all endpoints linked to the <strong>Profile</strong>.
 * <p>Version: <strong>V0</strong>.
 */
@RequestMapping("/profile")
public abstract class BaseProfileController<E extends BaseEntity, D extends BaseDto> extends BaseMeController<E, D> {
    public BaseProfileController(ResponseService responseService, MeFacade<E, D> facade) {
        super(responseService, facade);
    }
}
