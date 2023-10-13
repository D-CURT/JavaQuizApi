package com.quiz.javaquizapi.facade.me.user;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.BaseMeFacade;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.me.user.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides intermediary operations related to a {@link com.quiz.javaquizapi.model.user.User}.
 */
@Slf4j
@Facade
public class QuizUserFacade extends BaseMeFacade<User, UserDto> implements UserFacade {
    public QuizUserFacade(Mapper mapper) {
        super(null, mapper);
    }

    @Override
    public void create(UserDto dto) {
        log.info("Starting a new user authorization...");
        User user = mapper.map(dto, User.class);
//        service.create(user);
        mapper.map(user, dto);
        log.info("User authorization succeeded.");
    }
}
