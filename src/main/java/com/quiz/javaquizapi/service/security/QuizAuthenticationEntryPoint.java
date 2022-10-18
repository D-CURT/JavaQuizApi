package com.quiz.javaquizapi.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.javaquizapi.model.http.CommonErrors;
import com.quiz.javaquizapi.model.http.Response;
import com.quiz.javaquizapi.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provides error handling for the resources access related failures.
 */
@Component
@RequiredArgsConstructor
public class QuizAuthenticationEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ResponseService responseService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Response error = responseService.buildError(null, CommonErrors.AUTHENTICATION.name(), CommonErrors.AUTHENTICATION_REQUIRED);
        setResponseBody(response, HttpStatus.UNAUTHORIZED, error);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessException) throws IOException {
        Response error = responseService.buildError(null, CommonErrors.ACCESS.name(), CommonErrors.ACCESS_DENIED);
        setResponseBody(response, HttpStatus.FORBIDDEN, error);
    }

    private void setResponseBody(HttpServletResponse response, HttpStatus status, Response body) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        MAPPER.writeValue(response.getOutputStream(), body);
    }
}
