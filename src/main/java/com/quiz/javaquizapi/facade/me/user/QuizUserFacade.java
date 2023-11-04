package com.quiz.javaquizapi.facade.me.user;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dto.user.UserUpdateCodeDto;
import com.quiz.javaquizapi.dto.user.UserDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.BaseMeFacade;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.mail.UserUpdateService;
import com.quiz.javaquizapi.service.me.user.UserUpdateCodeService;
import com.quiz.javaquizapi.service.me.user.UserService;
import lombok.extern.slf4j.Slf4j;

import static com.quiz.javaquizapi.common.util.GenericUtils.cast;

/**
 * Provides intermediary operations related to a {@link User}.
 */
@Slf4j
@Facade
public class QuizUserFacade extends BaseMeFacade<User, UserDto> implements UserFacade {
    private final UserUpdateService updateService;
    private final UserUpdateCodeService codeService;

    public QuizUserFacade(UserService service, UserUpdateService passwordService, UserUpdateCodeService codeService,
                          Mapper mapper) {
        super(service, mapper);
        this.updateService = passwordService;
        this.codeService = codeService;
    }

    @Override
    public void create(UserDto dto) {
        log.info("Starting a new user authorization...");
        var user = mapper.map(dto, com.quiz.javaquizapi.model.user.User.class);
        service.create(user);
        mapper.map(user, dto);
        log.info("User authorization succeeded.");
    }

    @Override
    public void sendCodeToChangeUser(String username) {
        updateService.sendCode(username);
    }

    @Override
    public void updateMe(UserUpdateCodeDto dto) {
        var code = codeService.getMe(dto.getUsername());
        mapper.map(dto, code);
        updateService.updateUser(code.setUser(new User().setUsername(dto.getUsername())));
        log.info("The current user updated successfully.");
    }

    @Override
    public void updateRole(UserDto data) {
        var user = service.get(data.getCode());
        mapper.map(data, user);
        cast(service, UserService.class).update(user);
        log.info("User role have been changed to '{}'.", user.getRole());
    }

    @Override
    public void archive(String code) {
        log.info("Archiving a user...");
        var user = service.get(code);
        user.setEnabled(Boolean.FALSE);
        cast(service, UserService.class).update(user);
        log.info("User archived.");
    }
}
