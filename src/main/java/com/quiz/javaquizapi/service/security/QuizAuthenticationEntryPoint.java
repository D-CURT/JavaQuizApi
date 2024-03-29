package com.quiz.javaquizapi.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.javaquizapi.model.http.CommonErrors;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.service.response.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Provides error handling for the resources access related failures.
 */
@Component
@RequiredArgsConstructor
public class QuizAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ResponseService responseService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Response error = responseService.buildError(null, CommonErrors.AUTHENTICATION.name(), CommonErrors.AUTHENTICATION_REQUIRED);
        setResponseBody(response, HttpStatus.UNAUTHORIZED, error);
    }

    private void setResponseBody(HttpServletResponse response, HttpStatus status, Response body) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        MAPPER.writeValue(response.getOutputStream(), body);
    }
}
