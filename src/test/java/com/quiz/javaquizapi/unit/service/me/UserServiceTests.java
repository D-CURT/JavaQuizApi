package com.quiz.javaquizapi.unit.service.me;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.exception.ValidationException;
import com.quiz.javaquizapi.exception.user.UserExistsException;
import com.quiz.javaquizapi.exception.user.UserNotFoundException;
import com.quiz.javaquizapi.model.user.User;
import com.quiz.javaquizapi.service.me.user.UserService;
import com.quiz.javaquizapi.service.me.user.impl.QuizUserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("User service tests")
@ExtendWith(MockitoExtension.class)
public class UserServiceTests extends ApiTests {
    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService service;

    @BeforeEach
    void setUp() {
        initLog();
        service = new QuizUserService(repository, passwordEncoder);
    }

    @Test
    @DisplayName("Fetch me by incorrect username")
    public void testFetchingMeGivenIncorrectUsernameThenThrowUserNotFoundException() {
        var exception =
                Assertions.assertThrows(UserNotFoundException.class, () -> service.getMe("Wrong username"));
        assertThat(exception.getReason()).isEqualTo(UserNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getArgs()).contains("Wrong username").hasSize(1);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMe() {
        when(repository.findByUsername(localUser.getUsername())).thenReturn(Optional.of(localUser));
        User me = service.getMe(localUser.getUsername());
        assertThat(me).isNotNull();
        assertThat(me.getUsername()).isEqualTo(localUser.getUsername());
        verify(repository).findByUsername(localUser.getUsername());
        assertThat(captureLogs()).contains("Fetching a User by username...");
    }

    @Test
    @DisplayName("Fetch user by incorrect code")
    public void testFetchingUserByIncorrectCodeThenThrowUserNotFoundException() {
        var exception = Assertions.assertThrows(UserNotFoundException.class, () -> service.get("Wrong code"));
        assertThat(exception.getReason()).isEqualTo(UserNotFoundException.DEFAULT_ERROR);
        assertThat(exception.getArgs()).hasSize(0);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Fetch user by code")
    public void testFetchingUserGivenValidCode() {
        when(repository.findByCode(localUser.getCode())).thenReturn(Optional.of(localUser));
        User user = service.get(localUser.getCode());
        assertThat(user).isNotNull();
        assertThat(user.getCode()).isEqualTo(localUser.getCode());
        verify(repository).findByCode(localUser.getCode());
        assertThat(captureLogs()).contains("Fetching a User by code...");
    }

    @Test
    @DisplayName("Unable to create a user - already exists")
    public void testCreateUserGivenUserAlreadyExists() {
        when(repository.existsByUsername(localUser.getUsername())).thenReturn(Boolean.TRUE);
        UserExistsException exception = Assertions.assertThrows(UserExistsException.class, () -> service.create(localUser));
        assertThat(exception.getReason()).isEqualTo("Unable to create a user, such username already exists");
        assertThat(exception.getArgs()).contains(localUser.getUsername()).hasSize(1);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(repository).existsByUsername(localUser.getUsername());
    }

    @Test
    @DisplayName("Unable to create a user - provided code is invalid")
    public void testCreateUserGivenInvalidCode() {
        localUser.setCode("Wrong code");
        when(repository.existsByUsername(localUser.getUsername())).thenReturn(Boolean.FALSE);
        var exception = Assertions.assertThrows(ValidationException.class, () -> service.create(localUser));
        assertThat(exception.getReason())
                .isEqualTo("Unable to create one User: provided code is malformed," +
                        " check its format - UUID is required.");
        assertThat(exception.getArgs()).contains(localUser.getCode()).hasSize(1);
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        verify(repository).existsByUsername(localUser.getUsername());
    }

    @Test
    @DisplayName("Create a new user - all properties are preset")
    public void testCreateUserGivenAllPropertiesPreset() {
        var encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(localUser.getPassword());
        when(passwordEncoder.encode(localUser.getPassword()))
                .thenReturn(encodedPassword);
        var user = createUser();
        assertThat(user).isNotNull();
        assertThat(encoder.matches("password", user.getPassword())).isTrue();
        assertThat(user.getDisplayName()).isEqualTo("displayName");
        String logs = captureLogs();
        assertThat(logs).contains(
                "Checking if a user with given username already exists...",
                "Applying LOCAL authentication provider...",
                "Resolving a new entity unique code...",
                "Resolving a new user display name...",
                "Applying USER role...");
    }

    @Test
    @DisplayName("Create a new user - display name is null")
    public void testCreateUserGivenNullDisplayName() {
        localUser.setDisplayName(null);
        var user = createUser();
        assertThat(user).isNotNull();
        assertThat(user.getDisplayName()).doesNotContain("displayName").hasSize(10);
    }

    @Test
    @DisplayName("Create a new user - display name is empty")
    public void testCreateUserGivenEmptyDisplayName() {
        localUser.setDisplayName(StringUtils.EMPTY);
        var user = createUser();
        assertThat(user).isNotNull();
        assertThat(user.getDisplayName()).doesNotContain("displayName").hasSize(10);
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }

    private User createUser() {
        when(repository.existsByUsername(localUser.getUsername())).thenReturn(Boolean.FALSE);
        when(repository.save(any(User.class))).thenReturn(localUser);
        service.create(localUser);
        var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).existsByUsername(localUser.getUsername());
        verify(repository).save(userCaptor.capture());
        return userCaptor.getValue();
    }
}
