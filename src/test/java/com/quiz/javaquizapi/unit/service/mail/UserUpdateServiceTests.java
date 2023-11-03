package com.quiz.javaquizapi.unit.service.mail;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.exception.user.UserFieldSameValueException;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.model.user.UserUpdateCode;
import com.quiz.javaquizapi.model.user.UserUpdateType;
import com.quiz.javaquizapi.service.mail.MailSenderService;
import com.quiz.javaquizapi.service.mail.UserUpdateService;
import com.quiz.javaquizapi.service.mail.impl.QuizUserUpdateService;
import com.quiz.javaquizapi.service.me.user.UserService;
import com.quiz.javaquizapi.service.me.user.UserUpdateCodeService;
import com.quiz.javaquizapi.service.response.ResponseService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Change user update service tests")
public class UserUpdateServiceTests extends ApiTests {
    @Mock
    private UserUpdateCodeService codeService;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private MailSenderService mailService;
    @Mock
    private UserService userService;
    @Mock
    private MessageSource messageSource;
    private UserUpdateService service;

    @BeforeEach
    void setUp() {
        initLog();
        service = new QuizUserUpdateService(
                codeService,
                encoder,
                mailService,
                userService,
                messageSource);
    }

    @Test
    @DisplayName("Send user update code mail")
    public void testSendingUserUpdateCodeMailGivenCurrentUsername() {
        when(userService.getMe(localUser.getUsername())).thenReturn(localUser);
        service.sendCode(localUser.getUsername());
        var subjectKey = ResponseService.API_ERROR_CODE_PREFIX + QuizUserUpdateService.CHANGE_USER_SUBJECT;
        var bodyKey = ResponseService.API_ERROR_CODE_PREFIX + QuizUserUpdateService.CHANGE_USER_BODY;
        var arrayCaptor = ArgumentCaptor.forClass(String[].class);
        var codeCaptor = ArgumentCaptor.forClass(UserUpdateCode.class);
        verify(mailService).send(any(SimpleMailMessage.class));
        verify(userService).getMe(localUser.getUsername());
        verify(codeService).create(codeCaptor.capture());
        var updateCode = codeCaptor.getValue();
        assertThat(updateCode).isNotNull();
        assertThat(updateCode.getCheckNumber().length()).isEqualTo(QuizUserUpdateService.CODE_LENGTH);
        assertThat(updateCode.getUser()).isNotNull();
        assertThat(updateCode.getUser().getUsername()).isEqualTo(localUser.getUsername());
        verify(messageSource).getMessage(eq(subjectKey), arrayCaptor.capture(), any());
        var subjectArgs = arrayCaptor.getValue();
        verify(messageSource).getMessage(eq(bodyKey), arrayCaptor.capture(), any());
        var bodyArgs = arrayCaptor.getValue();
        assertThat(subjectArgs.length).isEqualTo(0);
        assertThat(bodyArgs.length).isEqualTo(1);
        assertThat(bodyArgs[0].length()).isEqualTo(QuizUserUpdateService.CODE_LENGTH);
        assertThat(captureLogs()).contains("Preparing an email message to send...");
    }

    @Test
    @DisplayName("Update current user password")
    public void testUpdatingUserPasswordGivenValidCode() {
        var newPass = "new user update";
        when(encoder.encode(newPass)).thenReturn(newPass);
        testUserUpdate(newPass, UserUpdateType.PASSWORD);
        verify(encoder).encode(newPass);
    }

    @Test
    @DisplayName("Update current user username")
    public void testUpdatingUserUsernameGivenValidCode() {
        var newUsername = "new user update";
        testUserUpdate(newUsername, UserUpdateType.USERNAME);
    }

    @Test
    @DisplayName("Update current user display name")
    public void testUpdatingUserDisplayNameGivenValidCode() {
        var newDisplayName = "new user update";
        testUserUpdate(newDisplayName, UserUpdateType.DISPLAY_NAME);
    }

    @Test
    @DisplayName("Update a user when new value is already in use")
    public void testUpdatingUserGivenTheSamePasswordThenThrowUserFieldValueInUseException() {
        when(userService.getMe(localUser.getUsername())).thenReturn(localUser);
        var expectedCode = RandomStringUtils.random(QuizUserUpdateService.CODE_LENGTH, false, true);
        var expected =
                new UserUpdateCode()
                        .setType(UserUpdateType.PASSWORD)
                        .setCheckNumber(expectedCode)
                        .setValue(localUser.getPassword())
                        .setUser(localUser);
        expected.setCreatedAt(LocalDateTime.now().minusMinutes(10));
        when(codeService.isValid(expected)).thenReturn(Boolean.TRUE);
        var exception = assertThrows(UserFieldSameValueException.class, () -> service.updateUser(expected));
        assertThat(exception.getReason()).isEqualTo(UserFieldSameValueException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(UserFieldSameValueException.VALUE_IN_USE_CODE);
        assertThat(exception.getArgs()).hasSize(0);
        verify(codeService).isValid(expected);
        verify(userService).getMe(localUser.getUsername());
        verify(userService, never()).update(any(User.class));
        verify(encoder, never()).encode(anyString());
        assertThat(captureLogs()).contains("Check if this user field value is already in use...");
    }

    private void testUserUpdate(String newValue, UserUpdateType type) {
        var username = localUser.getUsername();
        when(userService.getMe(username)).thenReturn(localUser);
        var expectedCode = RandomStringUtils.random(QuizUserUpdateService.CODE_LENGTH, false, true);
        var expected =
                new UserUpdateCode()
                        .setType(type)
                        .setCheckNumber(expectedCode)
                        .setValue(newValue)
                        .setUser(localUser);
        expected.setCreatedAt(LocalDateTime.now());
        when(codeService.isValid(expected)).thenReturn(Boolean.TRUE);
        var userCaptor = ArgumentCaptor.forClass(User.class);
        service.updateUser(expected);
        verify(codeService).isValid(expected);
        verify(codeService).update(expected);
        verify(userService).getMe(username);
        verify(userService).update(userCaptor.capture());
        var user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(type.getGetter().apply(user)).isEqualTo(newValue);
        assertThat(captureLogs())
                .contains(
                        "Check if this user field value is already in use...",
                        String.format("Saving a new %s value...", type));
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}
