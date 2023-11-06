package com.quiz.javaquizapi.unit.service.me;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.common.util.ValidationUtils;
import com.quiz.javaquizapi.dao.UserUpdateCodeRepository;
import com.quiz.javaquizapi.exception.user.UserUpdateCodeNotFoundException;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.model.user.UserUpdateCode;
import com.quiz.javaquizapi.service.BaseQuizService;
import com.quiz.javaquizapi.service.me.user.UserUpdateCodeService;
import com.quiz.javaquizapi.service.me.user.impl.QuizUserUpdateCodeService;
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
@DisplayName("User update code service tests")
public class UserUpdateCodeServiceTests extends ApiTests {
    private final UserUpdateCode testCode = new UserUpdateCode()
            .setCheckNumber("1234")
            .setValue("pass")
            .setUser(localUser);

    {
        localUser.setCode(UUID.randomUUID().toString());
    }

    @Mock
    private UserUpdateCodeRepository repository;
    private UserUpdateCodeService service;

    @BeforeEach
    void setUp() {
        initLog();
        service = new QuizUserUpdateCodeService(repository);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMeGivenCurrentUsername() {
        when(repository.findTopByUserUsernameOrderByCreatedAtDesc(localUser.getUsername()))
                .thenReturn(Optional.of(testCode));
        var actual = service.getMe(localUser.getUsername());
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(testCode);
        assertThat(captureLogs()).contains("Fetching a UserUpdateCode by username...");
        verify(repository).findTopByUserUsernameOrderByCreatedAtDesc(localUser.getUsername());
    }

    @Test
    @DisplayName("Fetch me by incorrect username")
    public void testFetchingMeGivenIncorrectUsernameThenThrowUserUpdateCodeNotFoundException() {
        when(repository.findTopByUserUsernameOrderByCreatedAtDesc("wrong username")).thenReturn(Optional.empty());
        var exception = assertThrows(
                UserUpdateCodeNotFoundException.class,
                () -> service.getMe("wrong username"));
        assertThat(exception.getReason()).isEqualTo(UserUpdateCodeNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(UserUpdateCodeNotFoundException.NOT_FOUND_CODE);
        assertThat(exception.getArgs()).hasSize(1);
        assertThat(exception.getArgs()).contains("wrong username");
        assertThat(captureLogs()).contains("Fetching a UserUpdateCode by username...");
        verify(repository).findTopByUserUsernameOrderByCreatedAtDesc("wrong username");
    }

    @Test
    @DisplayName("Fetch user update code by its code")
    public void testFetchingUserUpdateCodeGivenValidCode() {
        when(repository.findByCode(testCode.getCode())).thenReturn(Optional.of(testCode));
        var actual = service.get(testCode.getCode());
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(testCode);
        assertThat(captureLogs()).contains("Fetching a UserUpdateCode by code...");
        verify(repository).findByCode(testCode.getCode());
    }

    @Test
    @DisplayName("Fetch user update code by incorrect code")
    public void testFetchingUserUpdateCodeGivenIncorrectCodeThenThrowUserUpdateCodeNotFoundException() {
        when(repository.findByCode("wrong code")).thenReturn(Optional.empty());
        var exception = assertThrows(UserUpdateCodeNotFoundException.class, () -> service.get("wrong code"));
        assertThat(exception.getReason()).isEqualTo(UserUpdateCodeNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(UserUpdateCodeNotFoundException.NOT_FOUND_NO_ARGS_CODE);
        assertThat(exception.getArgs()).hasSize(0);
        assertThat(captureLogs()).contains("Fetching a UserUpdateCode by code...");
        verify(repository).findByCode("wrong code");
    }

    @Test
    @DisplayName("Create a new user update code")
    public void testCreatingUserUpdateCode() {
        var expected = new UserUpdateCode()
                .setCheckNumber(testCode.getCheckNumber())
                .setValue(testCode.getValue())
                .setUser(localUser);
        when(repository.save(any(UserUpdateCode.class))).thenReturn(testCode);
        var codeCaptor = ArgumentCaptor.forClass(UserUpdateCode.class);
        service.create(expected);
        verify(repository).save(codeCaptor.capture());
        var actual = codeCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getCheckNumber()).isEqualTo(expected.getCheckNumber());
        assertThat(actual.getValue()).isEqualTo(expected.getValue());
        assertThat(actual.getUser()).isEqualTo(localUser);
        assertThat(ValidationUtils
                .validator(BaseQuizService.CODE_ERROR_FORMAT.formatted(PersonalInfo.class.getSimpleName()))
                .validateCode(actual.getCode())
        ).isNotBlank();
        assertThat(captureLogs()).contains("Saving a user update code...");
    }

    @Test
    @DisplayName("Validate user update code")
    public void testValidatingUserUpdateCodeGivenValidCode() {
        testCode.setCreatedAt(LocalDateTime.now());
        when(repository.findTopByUserUsernameOrderByCreatedAtDesc(localUser.getUsername()))
                .thenReturn(Optional.of(testCode));
        assertTrue(service.isValid(testCode));
        assertThat(captureLogs()).contains("Validating user update code...");
    }

    @Test
    @DisplayName("Validate nonexistent user update code")
    public void testValidatingUserUpdateCodeGiveNonexistentUserUpdateCodeThenThrowUserUpdateCodeNotFoundException() {
        var expected = new UserUpdateCode().setUser(new User().setUsername("wrong username"));
        when(repository.findTopByUserUsernameOrderByCreatedAtDesc("wrong username"))
                .thenReturn(Optional.empty());
        var exception = assertThrows(UserUpdateCodeNotFoundException.class, () -> service.isValid(expected));
        assertThat(exception.getReason()).isEqualTo(UserUpdateCodeNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getCode()).isEqualTo(UserUpdateCodeNotFoundException.NOT_FOUND_CODE);
        assertThat(exception.getArgs()).hasSize(1);
        assertThat(exception.getArgs()).contains("wrong username");
        verify(repository).findTopByUserUsernameOrderByCreatedAtDesc("wrong username");
        assertThat(captureLogs()).contains("Validating user update code...");
    }

    @Test
    @DisplayName("Validate expired user update code")
    public void testValidatingUserUpdateCodeGivenExpiredUserUpdateCode() {
        testCode.setCreatedAt(LocalDateTime.now().minusMinutes(10));
        when(repository.findTopByUserUsernameOrderByCreatedAtDesc(localUser.getUsername()))
                .thenReturn(Optional.of(testCode));
        assertFalse(service.isValid(testCode));
        assertThat(captureLogs()).contains("Validating user update code...");
        verify(repository).findTopByUserUsernameOrderByCreatedAtDesc(localUser.getUsername());
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}
