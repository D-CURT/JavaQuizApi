package com.quiz.javaquizapi.integration.dao;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User DAO tests")
public class UserTests extends DaoTests {

    public static User testUser;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        testUser = new User().setUsername("username").setPassword("password").setDisplayName("displayName").setRole(Roles.USER).setProvider(Providers.LOCAL).setEnabled(Boolean.TRUE);
        testUser.setCode(UUID.randomUUID().toString());
        repository.save(testUser);
    }

    @Test
    @DisplayName("Fetch an existing user by its username")
    public void testFetchingExistingUser() {
        repository.findByUsername(testUser.getUsername())
                .ifPresentOrElse(user -> {
                    assertThat(user).isNotNull();
                    assertThat(user.getUsername()).isEqualTo(testUser.getUsername());
                    assertThat(user.getPassword()).isEqualTo("password");
                }, () -> Assertions.fail("User not found"));
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch non-existing user by its username")
    public void testFetchingNonExistingUser() {
        assertThat(repository.findByUsername("Wrong username").isEmpty()).isTrue();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Check if user exists by its username. User exists")
    public void testExistingUser() {
        assertThat(repository.existsByUsername(testUser.getUsername())).isTrue();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Check if user exists by its username. User doesn't exist")
    public void testNonExistingUser() {
        assertThat(repository.existsByUsername("Wrong username")).isFalse();
        assertExecutedQueries();
    }
}
