package com.quiz.javaquizapi.facade.impl;

import lombok.RequiredArgsConstructor;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.Mapper;
import com.quiz.javaquizapi.facade.UserFacade;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.user.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides intermediary operations related to a {@link com.quiz.javaquizapi.model.user.User}.
 */
@Slf4j
@Facade
@RequiredArgsConstructor
public class QuizUserFacade implements UserFacade {

    private final UserService service;
    private final Mapper mapper;

    @Override
    public void authorize(UserDto dto) {
        log.info("Starting a new user authorization...");
        User user = mapper.map(dto, User.class);
        service.create(user);
        dto.setCode(user.getCode());
        log.info("User authorization succeeded.");
    }
}
