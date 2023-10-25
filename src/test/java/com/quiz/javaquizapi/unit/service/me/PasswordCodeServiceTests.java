package com.quiz.javaquizapi.unit.service.me;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.common.utils.ValidationUtils;
import com.quiz.javaquizapi.dao.PasswordCodeRepository;
import com.quiz.javaquizapi.exception.user.PasswordCodeNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.model.user.PasswordCode;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.me.user.PasswordCodeService;
import com.quiz.javaquizapi.service.me.user.impl.QuizPasswordCodeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Password code service tests")
public class PasswordCodeServiceTests extends ApiTests {
    private final PasswordCode testCode = new PasswordCode()
            .setPasswordCode("1234")
            .setPassword("pass")
            .setUser(localUser);

    {
        localUser.setCode(UUID.randomUUID().toString());
    }

    @Mock
    private PasswordCodeRepository repository;
    private PasswordCodeService service;

    @BeforeEach
    void setUp() {
        initLog();
        service = new QuizPasswordCodeService(repository);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMeGivenCurrentUsername() {
        when(repository.findTopByUserUsername(localUser.getUsername())).thenReturn(Optional.of(testCode));
        var actual = service.getMe(localUser.getUsername());
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(testCode);
        assertThat(captureLogs()).contains("Fetching a PasswordCode by username...");
        verify(repository).findTopByUserUsername(localUser.getUsername());
    }

    @Test
    @DisplayName("Fetch me by incorrect username")
    public void testFetchingMeGivenIncorrectUsernameThenThrowPasswordCodeNotFoundException() {
        when(repository.findTopByUserUsername("wrong username")).thenReturn(Optional.empty());
        var exception = assertThrows(
                PasswordCodeNotFoundException.class,
                () -> service.getMe("wrong username"));
        assertThat(exception.getReason()).isEqualTo(PasswordCodeNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(PasswordCodeNotFoundException.PASSWORD_CODE_NOT_FOUND_CODE);
        assertThat(exception.getArgs()).hasSize(1);
        assertThat(exception.getArgs()).contains("wrong username");
        assertThat(captureLogs()).contains("Fetching a PasswordCode by username...");
        verify(repository).findTopByUserUsername("wrong username");
    }

    @Test
    @DisplayName("Fetch password code by its code")
    public void testFetchingPasswordCodeGivenValidCode() {
        when(repository.findByCode(testCode.getCode())).thenReturn(Optional.of(testCode));
        var actual = service.get(testCode.getCode());
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(testCode);
        assertThat(captureLogs()).contains("Fetching a PasswordCode by code...");
        verify(repository).findByCode(testCode.getCode());
    }

    @Test
    @DisplayName("Fetch password code by incorrect code")
    public void testFetchingPasswordCodeGivenIncorrectCodeThenThrowPasswordCodeNotFoundException() {
        when(repository.findByCode("wrong code")).thenReturn(Optional.empty());
        var exception = assertThrows(PasswordCodeNotFoundException.class, () -> service.get("wrong code"));
        assertThat(exception.getReason()).isEqualTo(PasswordCodeNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(PasswordCodeNotFoundException.PASSWORD_CODE_NOT_FOUND_NO_ARGS_CODE);
        assertThat(exception.getArgs()).hasSize(0);
        assertThat(captureLogs()).contains("Fetching a PasswordCode by code...");
        verify(repository).findByCode("wrong code");
    }

    @Test
    @DisplayName("Create a new Password code")
    public void testCreatingPasswordCode() {
        var expected = new PasswordCode()
                .setPasswordCode(testCode.getPasswordCode())
                .setPassword(testCode.getPassword())
                .setUser(localUser);
        when(repository.save(any(PasswordCode.class))).thenReturn(testCode);
        var codeCaptor = ArgumentCaptor.forClass(PasswordCode.class);
        service.create(expected);
        verify(repository).save(codeCaptor.capture());
        var actual = codeCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getPasswordCode()).isEqualTo(expected.getPasswordCode());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getUser()).isEqualTo(localUser);
        assertThat(ValidationUtils
                .validator(BaseQuizService.CODE_ERROR_FORMAT.formatted(PersonalInfo.class.getSimpleName()))
                .validateCode(actual.getCode())
        ).isNotBlank();
        assertThat(captureLogs()).contains("Saving a password code...");
    }

    @Test
    @DisplayName("Validate password code")
    public void testValidatingPasswordCodeGivenValidCode() {
        testCode.setCreatedAt(LocalDateTime.now());
        when(repository.findTopByUserUsername(localUser.getUsername())).thenReturn(Optional.of(testCode));
        assertTrue(service.isValid(testCode));
        assertThat(captureLogs()).contains("Validating password code...");
    }

    @Test
    @DisplayName("Validate nonexistent password code")
    public void testValidatingPasswordCodeGiveNonexistentPasswordCodeThenThrowPasswordCodeNotFoundException() {
        var expected = new PasswordCode().setUser(new User().setUsername("wrong username"));
        when(repository.findTopByUserUsername("wrong username")).thenReturn(Optional.empty());
        var exception = assertThrows(PasswordCodeNotFoundException.class, () -> service.isValid(expected));
        assertThat(exception.getReason()).isEqualTo(PasswordCodeNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(PasswordCodeNotFoundException.PASSWORD_CODE_NOT_FOUND_CODE);
        assertThat(exception.getArgs()).hasSize(1);
        assertThat(exception.getArgs()).contains("wrong username");
        verify(repository).findTopByUserUsername("wrong username");
        assertThat(captureLogs()).contains("Validating password code...");
    }

    @Test
    @DisplayName("Validate expired password code")
    public void testValidatingPasswordCodeGivenExpiredPasswordCode() {
        testCode.setCreatedAt(LocalDateTime.now().minusMinutes(10));
        when(repository.findTopByUserUsername(localUser.getUsername())).thenReturn(Optional.of(testCode));
        assertFalse(service.isValid(testCode));
        assertThat(captureLogs()).contains("Validating password code...");
        verify(repository).findTopByUserUsername(localUser.getUsername());
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}
