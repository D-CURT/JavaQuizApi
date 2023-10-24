package com.quiz.javaquizapi.integration.facade.me;

import com.quiz.javaquizapi.dto.UserDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.user.QuizUserFacade;
import com.quiz.javaquizapi.facade.me.user.UserFacade;
import com.quiz.javaquizapi.integration.ApiIntegrationTests;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.me.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@DisplayName("User facade tests")
public class UserFacadeTests extends ApiIntegrationTests {
    @Mock
    private UserService service;
    @Autowired
    private Mapper mapper;
    private UserFacade facade;

    @BeforeEach
    void setUp() {
        initLog();
        facade = new QuizUserFacade(service, mapper);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMe() {
        when(service.getMe(localUser.getUsername())).thenReturn(localUser);
        UserDto me = facade.getMe(localUser.getUsername());
        assertThat(me).isNotNull();
        assertThat(me.getUsername()).isNull();
        assertThat(me.getPassword()).isNull();
        assertThat(me.getDisplayName()).isEqualTo(localUser.getDisplayName());
        assertThat(me.getCode()).isEqualTo(localUser.getCode());
        verify(service).getMe(localUser.getUsername());
    }

    @Test
    @DisplayName("Create user")
    public void testCreationMe() {
        doNothing().when(service).create(any(User.class));
        var dto = new UserDto();
        dto.setUsername(localUser.getUsername());
        facade.create(dto.setPassword(localUser.getPassword()));
        var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(service).create(userCaptor.capture());
        var user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(localUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(localUser.getPassword());
        assertThat(captureLogs()).contains("Starting a new user authorization...", "User authorization succeeded.");
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}

