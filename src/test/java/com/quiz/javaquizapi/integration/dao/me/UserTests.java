package com.quiz.javaquizapi.integration.dao.me;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.integration.dao.MeDaoTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User DAO tests")
public class UserTests extends MeDaoTests<UserRepository> {
    @Test
    @DisplayName("Fetch an existing user by its username")
    public void testFetchingExistingUserByUsername() {
        getRepository().findByUsername(localUser.getUsername())
                .ifPresentOrElse(user -> {
                    assertThat(user).isNotNull();
                    assertThat(user.getUsername()).isEqualTo(localUser.getUsername());
                    assertThat(user.getPassword()).isEqualTo("password");
                }, () -> Assertions.fail("User not found"));
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch non-existing user by username")
    public void testFetchingNonExistingUserByUsername() {
        assertThat(getRepository().findByUsername("Wrong username").isEmpty()).isTrue();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch an existing user by its code")
    public void testFetchingExistingUserByCode() {
        getRepository().findByCode(localUser.getCode())
                .ifPresentOrElse(user -> {
                    assertThat(user).isNotNull();
                    assertThat(user.getCode()).isEqualTo(localUser.getCode());
                    assertThat(user.getUsername()).isEqualTo(localUser.getUsername());
                    assertThat(user.getPassword()).isEqualTo("password");
                }, () -> Assertions.fail("User not found"));
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Fetch non-existing user by code")
    public void testFetchingNonExistingUserByCode() {
        assertThat(getRepository().findByCode("Wrong code").isEmpty()).isTrue();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Check if user exists by its username. User exists")
    public void testExistingUserByUsername() {
        assertThat(getRepository().existsByUsername(localUser.getUsername())).isTrue();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Check if user exists by its username. User doesn't exist")
    public void testNonExistingUserByUsername() {
        assertThat(getRepository().existsByUsername("Wrong username")).isFalse();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Check if user exists by its code. User exists")
    public void testExistingUserByCode() {
        assertThat(getRepository().existsByCode(localUser.getCode())).isTrue();
        assertExecutedQueries();
    }

    @Test
    @DisplayName("Check if user exists by its code. User doesn't exist")
    public void testNonExistingUserByCode() {
        assertThat(getRepository().existsByCode("Wrong code")).isFalse();
        assertExecutedQueries();
    }
}
