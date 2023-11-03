package com.quiz.javaquizapi.integration.facade.me;

import com.quiz.javaquizapi.dto.user.UserUpdateCodeDto;
import com.quiz.javaquizapi.dto.user.UserDto;
import com.quiz.javaquizapi.facade.mapping.Mapper;
import com.quiz.javaquizapi.facade.me.user.QuizUserFacade;
import com.quiz.javaquizapi.facade.me.user.UserFacade;
import com.quiz.javaquizapi.integration.ApiIntegrationTests;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.UserUpdateCode;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.model.user.UserUpdateType;
import com.quiz.javaquizapi.service.mail.UserUpdateService;
import com.quiz.javaquizapi.service.me.user.UserUpdateCodeService;
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

import static com.quiz.javaquizapi.service.mail.impl.QuizUserUpdateService.CODE_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@DisplayName("User facade tests")
public class UserFacadeTests extends ApiIntegrationTests {
    @Mock
    private UserUpdateService updateService;
    @Mock
    private UserUpdateCodeService codeService;
    @Mock
    private UserService service;
    @Autowired
    private Mapper mapper;
    private UserFacade facade;

    @BeforeEach
    void setUp() {
        initLog();
        facade = new QuizUserFacade(service, updateService, codeService, mapper);
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
    @DisplayName("Send user update code by email")
    public void testSendingUserUpdateCodeGivenCurrentUsername() {
        facade.sendCodeToChangeUser(localUser.getUsername());
        verify(updateService).sendCode(localUser.getUsername());
    }

    @Test
    @DisplayName("Change user password")
    public void testChangingPasswordGivenCurrentUsername() {
        var expectedCode = RandomStringUtils.random(CODE_LENGTH, false, true);
        var expectedPassword = "newPass";
        var dto = new UserUpdateCodeDto()
                .setType(UserUpdateType.PASSWORD)
                .setValue(expectedPassword)
                .setCheckNumber(expectedCode);
        dto.setUsername(localUser.getUsername());
        var entity =
                new UserUpdateCode()
                        .setCheckNumber(expectedCode)
                        .setUser(localUser);
        entity.setCode(UUID.randomUUID().toString());
        entity.setCreatedAt(LocalDateTime.now());
        when(codeService.getMe(localUser.getUsername())).thenReturn(entity);
        var expectedUser = new com.quiz.javaquizapi.model.user.User().setUsername(dto.getUsername());
        entity.setUser(expectedUser);
        facade.updateMe(dto);
        var codeCaptor = ArgumentCaptor.forClass(UserUpdateCode.class);
        verify(updateService).updateUser(codeCaptor.capture());
        var actualCode = codeCaptor.getValue();
        assertThat(actualCode).isNotNull();
        assertThat(actualCode.getType()).isEqualTo(dto.getType());
        assertThat(actualCode.getValue()).isEqualTo(expectedPassword);
        assertThat(actualCode.getUser().getUsername()).isEqualTo(expectedUser.getUsername());
        verify(codeService).getMe(dto.getUsername());
        assertThat(captureLogs()).contains("The current user updated successfully.");
    }

    @Test
    @DisplayName("Change user role")
    public void testChangingUserRoleGivenValidCodeAndRole() {
        when(service.get(localUser.getCode())).thenReturn(localUser);
        var data = new UserDto().setRole(Roles.ADMIN);
        data.setCode(localUser.getCode());
        facade.updateRole(data);
        var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(service).update(userCaptor.capture());
        var actual = userCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getRole()).isEqualTo(data.getRole());
        assertThat(captureLogs()).contains("User role have been changed to 'ADMIN'.");
    }

    @Test
    @DisplayName("Archive a user")
    public void testArchivingUserGivenValidUserEntity() {
        when(service.get(localUser.getCode())).thenReturn(localUser);
        facade.archive(localUser.getCode());
        var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(service).update(userCaptor.capture());
        var user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(user.getEnabled()).isFalse();
        assertThat(captureLogs()).contains("Archiving a user...", "User archived.");
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}

