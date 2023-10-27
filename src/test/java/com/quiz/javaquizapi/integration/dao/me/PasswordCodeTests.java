package com.quiz.javaquizapi.integration.dao.me;

import com.quiz.javaquizapi.dao.PasswordCodeRepository;
import com.quiz.javaquizapi.integration.dao.MeDaoTests;
import com.quiz.javaquizapi.model.user.PasswordCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Password code DAO tests")
public class PasswordCodeTests extends MeDaoTests<PasswordCodeRepository> {
    private PasswordCode testCode;

    @BeforeEach
    void setUp() {
        testCode = new PasswordCode().setUser(localUser).setPassword("pass").setCheckNumber("1234");
        testCode.setCode(UUID.randomUUID().toString());
        getRepository().save(testCode);
    }

    @Test
    @DisplayName("Fetch me by username")
    public void testFetchingMeGivenCurrentUsername() {
        getRepository().findTopByUserUsernameOrderByCreatedAtDesc(localUser.getUsername())
                .ifPresentOrElse(code -> {
                        assertThat(code).isNotNull();
                        assertThat(code.getCode()).isEqualTo(testCode.getCode());
                        assertThat(code.getCheckNumber()).isEqualTo(testCode.getCheckNumber());
                        assertThat(code.getPassword()).isEqualTo(testCode.getPassword());
                        assertThat(code.getUser()).isEqualTo(localUser);
                }, () -> Assertions.fail("Password code not found"));
    }
}
