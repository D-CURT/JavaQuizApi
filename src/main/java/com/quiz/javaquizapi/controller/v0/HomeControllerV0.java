package com.quiz.javaquizapi.controller.v0;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeControllerV0 {

    @Operation(summary = "Page in progress")
    @GetMapping(StringUtils.EMPTY)
    public String home() {
        return "Welcome to Java Quiz API!";
    }
}
