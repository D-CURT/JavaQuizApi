package com.quiz.javaquizapi.unit.service.mail;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.exception.user.PasswordInUseException;
import com.quiz.javaquizapi.model.user.PasswordCode;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.mail.ChangePasswordService;
import com.quiz.javaquizapi.service.mail.MailSenderService;
import com.quiz.javaquizapi.service.mail.impl.QuizChangePasswordService;
import com.quiz.javaquizapi.service.me.user.PasswordCodeService;
import com.quiz.javaquizapi.service.me.user.UserService;
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
@DisplayName("Change password service tests")
public class ChangePasswordServiceTests extends ApiTests {
    @Mock
    private PasswordCodeService codeService;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private MailSenderService mailService;
    @Mock
    private UserService userService;
    @Mock
    private MessageSource messageSource;
    private ChangePasswordService service;

    @BeforeEach
    void setUp() {
        initLog();
        service = new QuizChangePasswordService(
                codeService,
                encoder,
                mailService,
                userService,
                messageSource);
    }

    @Test
    @DisplayName("Send password code mail")
    public void testSendingPasswordCodeMailGivenCurrentUsername() {
        when(userService.getMe(localUser.getUsername())).thenReturn(localUser);
        service.sendCode(localUser.getUsername());
        var subjectKey = ResponseService.API_ERROR_CODE_PREFIX + QuizChangePasswordService.CHANGE_PASS_SUBJECT;
        var bodyKey = ResponseService.API_ERROR_CODE_PREFIX + QuizChangePasswordService.CHANGE_PASS_BODY;
        var arrayCaptor = ArgumentCaptor.forClass(String[].class);
        var codeCaptor = ArgumentCaptor.forClass(PasswordCode.class);
        verify(mailService).send(any(SimpleMailMessage.class));
        verify(userService).getMe(localUser.getUsername());
        verify(codeService).create(codeCaptor.capture());
        var passwordCode = codeCaptor.getValue();
        assertThat(passwordCode).isNotNull();
        assertThat(passwordCode.getCheckNumber().length()).isEqualTo(QuizChangePasswordService.CODE_LENGTH);
        assertThat(passwordCode.getUser()).isNotNull();
        assertThat(passwordCode.getUser().getUsername()).isEqualTo(localUser.getUsername());
        verify(messageSource).getMessage(eq(subjectKey), arrayCaptor.capture(), any());
        var subjectArgs = arrayCaptor.getValue();
        verify(messageSource).getMessage(eq(bodyKey), arrayCaptor.capture(), any());
        var bodyArgs = arrayCaptor.getValue();
        assertThat(subjectArgs.length).isEqualTo(0);
        assertThat(bodyArgs.length).isEqualTo(1);
        assertThat(bodyArgs[0].length()).isEqualTo(QuizChangePasswordService.CODE_LENGTH);
        assertThat(captureLogs()).contains("Preparing an email message to send...");
    }

    @Test
    @DisplayName("Change password for the current user")
    public void testChangingPasswordGivenValidCode() {
        var newPass = "new password";
        when(userService.getMe(localUser.getUsername())).thenReturn(localUser);
        when(encoder.encode(newPass)).thenReturn(newPass);
        var expectedCode = RandomStringUtils.random(QuizChangePasswordService.CODE_LENGTH, false, true);
        var expected =
                new PasswordCode()
                        .setCheckNumber(expectedCode)
                        .setPassword(newPass)
                        .setUser(localUser);
        expected.setCreatedAt(LocalDateTime.now());
        when(codeService.isValid(expected)).thenReturn(Boolean.TRUE);
        var userCaptor = ArgumentCaptor.forClass(User.class);
        service.changePassword(expected);
        verify(codeService).isValid(expected);
        verify(userService).getMe(localUser.getUsername());
        verify(userService).update(userCaptor.capture());
        verify(encoder).encode(newPass);
        var user = userCaptor.getValue();
        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo(newPass);
        assertThat(captureLogs()).contains("Check if the password is already in use...", "Saving a new password...");
    }

    @Test
    @DisplayName("Change password when the new one is already in use")
    public void testChangingPasswordGivenTheSamePasswordThenThrowPasswordInUseException() {
        when(userService.getMe(localUser.getUsername())).thenReturn(localUser);
        var expectedCode = RandomStringUtils.random(QuizChangePasswordService.CODE_LENGTH, false, true);
        var expected =
                new PasswordCode()
                        .setCheckNumber(expectedCode)
                        .setPassword(localUser.getPassword())
                        .setUser(localUser);
        expected.setCreatedAt(LocalDateTime.now().minusMinutes(10));
        when(codeService.isValid(expected)).thenReturn(Boolean.TRUE);
        var exception = assertThrows(PasswordInUseException.class, () -> service.changePassword(expected));
        assertThat(exception.getReason()).isEqualTo(PasswordInUseException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(PasswordInUseException.PASSWORD_IN_USE_CODE);
        assertThat(exception.getArgs()).hasSize(0);
        verify(codeService).isValid(expected);
        verify(userService).getMe(localUser.getUsername());
        verify(userService, never()).update(any(User.class));
        verify(encoder, never()).encode(anyString());
        assertThat(captureLogs()).contains("Check if the password is already in use...");
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}
