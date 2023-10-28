package com.quiz.javaquizapi.integration.facade.me;

import com.quiz.javaquizapi.dto.user.PasswordCodeDto;
import com.quiz.javaquizapi.dto.user.UserDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.user.QuizUserFacade;
import com.quiz.javaquizapi.facade.me.user.UserFacade;
import com.quiz.javaquizapi.integration.ApiIntegrationTests;
import com.quiz.javaquizapi.model.user.PasswordCode;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.mail.ChangePasswordService;
import com.quiz.javaquizapi.service.me.user.PasswordCodeService;
import com.quiz.javaquizapi.service.me.user.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.quiz.javaquizapi.service.mail.impl.QuizChangePasswordService.CODE_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@DisplayName("User facade tests")
public class UserFacadeTests extends ApiIntegrationTests {
    @Mock
    private ChangePasswordService passwordService;
    @Mock
    private PasswordCodeService codeService;
    @Mock
    private UserService service;
    @Autowired
    private Mapper mapper;
    private UserFacade facade;

    @BeforeEach
    void setUp() {
        initLog();
        facade = new QuizUserFacade(service, passwordService, codeService, mapper);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMe() {
        when(service.getMe(localUser.getUsername())).thenReturn(localUser);
        var me = facade.getMe(localUser.getUsername());
        assertThat(me).isNotNull();
        assertThat(me.getEnabled()).isNull();
        assertThat(me.getUsername()).isNull();
        assertThat(me.getPassword()).isNull();
        assertThat(me.getDisplayName()).isEqualTo(localUser.getDisplayName());
        assertThat(me.getCode()).isEqualTo(localUser.getCode());
        verify(service).getMe(localUser.getUsername());
    }

    @Test
    @DisplayName("Create user")
    public void testCreationMe() {
        doNothing().when(service).create(any(com.quiz.javaquizapi.model.user.User.class));
        var dto = new UserDto();
        dto.setUsername(localUser.getUsername());
        facade.create(dto.setPassword(localUser.getPassword()));
        var userCaptor = ArgumentCaptor.forClass(com.quiz.javaquizapi.model.user.User.class);
        verify(service).create(userCaptor.capture());
        assertThat(dto.getUsername()).isNull();
        assertThat(dto.getPassword()).isNull();
        var user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(localUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(localUser.getPassword());
        assertThat(captureLogs()).contains("Starting a new user authorization...", "User authorization succeeded.");
    }

    @Test
    @DisplayName("Send password code by email")
    public void testSendingPasswordCodeGivenCurrentUsername() {
        facade.sendCodeToChangePassword(localUser.getUsername());
        verify(passwordService).sendCode(localUser.getUsername());
    }

    @Test
    @DisplayName("Change user password")
    public void testChangingPasswordGivenCurrentUsername() {
        var expectedCode = RandomStringUtils.random(CODE_LENGTH, false, true);
        var expectedPassword = "newPass";
        var dto = new PasswordCodeDto()
                .setPassword(expectedPassword)
                .setCheckNumber(expectedCode);
        dto.setUsername(localUser.getUsername());
        var entity =
                new PasswordCode()
                        .setCheckNumber(expectedCode)
                        .setUser(localUser);
        entity.setCode(UUID.randomUUID().toString());
        entity.setCreatedAt(LocalDateTime.now());
        when(codeService.getMe(localUser.getUsername())).thenReturn(entity);
        var expectedUser = new com.quiz.javaquizapi.model.user.User().setUsername(dto.getUsername());
        entity.setUser(expectedUser);
        facade.changePassword(dto);
        var codeCaptor = ArgumentCaptor.forClass(PasswordCode.class);
        verify(passwordService).changePassword(codeCaptor.capture());
        var actualCode = codeCaptor.getValue();
        assertThat(actualCode).isNotNull();
        assertThat(actualCode.getPassword()).isEqualTo(expectedPassword);
        assertThat(actualCode.getUser().getUsername()).isEqualTo(expectedUser.getUsername());
        verify(codeService).getMe(dto.getUsername());
        assertThat(captureLogs()).contains("The current user password changed successfully.");
    }

    @Test
    @DisplayName("Update a user")
    public void testUpdatingUserGivenValidUserEntity() {
        when(service.get(localUser.getCode())).thenReturn(localUser);
        var data = new UserDto().setDisplayName("updated display name");
        data.setCode(localUser.getCode());
        facade.update(data);
        var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(service).update(userCaptor.capture());
        var user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(user.getDisplayName()).isEqualTo(data.getDisplayName());
        assertThat(captureLogs()).contains("Updating a user...", "User update succeeded.");
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}

