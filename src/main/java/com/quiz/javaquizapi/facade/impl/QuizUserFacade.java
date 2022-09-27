package com.quiz.javaquizapi.facade.impl;

import com.quiz.javaquizapi.annotation.Facade;
import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.Mapper;
import com.quiz.javaquizapi.facade.UserFacade;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.user.UserService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class QuizUserFacade implements UserFacade {

    private final UserService service;
    private final Mapper mapper;

    @Override
    public void authorize(UserDto dto) {
        service.create(mapper.map(dto, User.class));
    }
}
