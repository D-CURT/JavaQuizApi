package com.quiz.javaquizapi.integration.dao.me;

import com.quiz.javaquizapi.dao.UserUpdateCodeRepository;
import com.quiz.javaquizapi.integration.dao.MeDaoTests;
import com.quiz.javaquizapi.model.user.UserUpdateCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User update code DAO tests")
public class UserUpdateCodeTests extends MeDaoTests<UserUpdateCodeRepository> {
    private UserUpdateCode testCode;

    @BeforeEach
    void setUp() {
        testCode = new UserUpdateCode().setUser(localUser).setValue("pass").setCheckNumber("1234");
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
                        assertThat(code.getValue()).isEqualTo(testCode.getValue());
                        assertThat(code.getUser()).isEqualTo(localUser);
                }, () -> Assertions.fail("User update code not found"));
    }
}
