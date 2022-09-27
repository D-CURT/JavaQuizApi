package com.quiz.javaquizapi.controller.v0;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeControllerV0 {

    @GetMapping(StringUtils.EMPTY)
    public String home(){
        return "Welcome to Java Quiz API!";
    }
}
