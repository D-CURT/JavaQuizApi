package com.quiz.javaquizapi.facade.me.user;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dto.user.UserDto;
import com.quiz.javaquizapi.dto.user.PasswordCodeDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.BaseMeFacade;
import com.quiz.javaquizapi.model.user.PasswordCode;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.mail.ChangePasswordService;
import com.quiz.javaquizapi.service.me.user.PasswordCodeService;
import com.quiz.javaquizapi.service.me.user.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides intermediary operations related to a {@link com.quiz.javaquizapi.model.user.User}.
 */
@Slf4j
@Facade
public class QuizUserFacade extends BaseMeFacade<User, UserDto> implements UserFacade {
    private final ChangePasswordService passwordService;
    private final PasswordCodeService codeService;

    public QuizUserFacade(UserService service, ChangePasswordService passwordService, PasswordCodeService codeService,
                          Mapper mapper) {
        super(service, mapper);
        this.passwordService = passwordService;
        this.codeService = codeService;
    }

    @Override
    public void create(UserDto dto) {
        log.info("Starting a new user authorization...");
        User user = mapper.map(dto, User.class);
        service.create(user);
        mapper.map(user, dto);
        log.info("User authorization succeeded.");
    }

    @Override
    public void sendCodeToChangePassword(String username) {
        passwordService.sendCode(username);
    }

    @Override
    public void changePassword(PasswordCodeDto dto) {
        PasswordCode code = codeService.getMe(dto.getUsername());
        mapper.map(dto, code);
        passwordService.changePassword(code.setUser(new User().setUsername(dto.getUsername())));
        log.info("The current user password changed successfully.");
    }
}
