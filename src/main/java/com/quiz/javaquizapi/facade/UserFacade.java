package com.quiz.javaquizapi.facade;

import com.quiz.javaquizapi.dto.UserDto;

public interface UserFacade {
    void authorize(UserDto dto);
}
