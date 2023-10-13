package com.quiz.javaquizapi.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.javaquizapi.model.http.CommonErrors;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.service.response.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Provides authentication failures functionality.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuizAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper;
    private final ResponseService responseService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Response error = responseService.buildError(null, CommonErrors.AUTHENTICATION.name(), CommonErrors.BAD_CREDENTIALS);
        log.error("Authentication failed:", exception);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        mapper.writeValue(response.getOutputStream(), error);
    }
}
