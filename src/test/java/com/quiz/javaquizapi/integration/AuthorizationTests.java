package com.quiz.javaquizapi.integration;

import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.endpoint.RandomPortTests;
import com.quiz.javaquizapi.model.http.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationTests extends RandomPortTests {

    @Test
    public void testAuthorization() {
        var expectedUsername = localUser.getUsername();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(new UserDto().setUsername(expectedUsername).setPassword(localUser.getPassword()), headers);
        Response response = template.postForObject(buildUrl("/user/authorization"), entity, Response.class);
        assertThat(response).isNotNull();
        assertThat(response.getError()).isNull();
        assertThat(response.getData()).isNotNull();
        UserDto body = objectMapper.convertValue(response.getData(), UserDto.class);
        assertThat(body.getUsername()).isEqualTo(expectedUsername);
        assertThat(body.getPassword()).isNull();
        response = template.getForObject(buildUrl("user"), Response.class);
        assertThat(response).isNotNull();
    }
}

