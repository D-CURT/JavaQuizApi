package com.quiz.javaquizapi.controller.v0;

import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserControllerV0 {

    private final UserFacade facade;

    @PutMapping(StringUtils.EMPTY)
    public UserDto authorize(@RequestBody UserDto dto) {
        facade.authorize(dto);
        return dto;
    }
}
