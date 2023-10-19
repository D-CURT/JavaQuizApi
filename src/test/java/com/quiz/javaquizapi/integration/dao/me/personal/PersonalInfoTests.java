package com.quiz.javaquizapi.integration.dao.me.personal;

import com.quiz.javaquizapi.dao.PersonalInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@DisplayName("Personal Info DAO tests")
public class PersonalInfoTests extends PersonalDaoTests<PersonalInfoRepository> {

    @Test
    @DisplayName("Fetch an existing personal info by existing profile code")
    public void testFetchingExistingPersonalInfo() {
        getRepository().findByProfileCode(PROFILE_CODE)
                .ifPresentOrElse(info -> {
                    assertThat(info).isNotNull();
                    assertThat(info.getProfile().getCode()).isEqualTo(PROFILE_CODE);
                    assertThat(info.getCode()).isEqualTo(getTestInfo().getCode());
                }, () -> Assertions.fail("Personal Info not found"));
    }

    @Test
    @DisplayName("Check if A personal info exists by its profile code")
    public void testExistingPersonalInfoByProfileCode() {
        assertTrue(getInfoRepository().existsByProfileCode(PROFILE_CODE));
    }
}
