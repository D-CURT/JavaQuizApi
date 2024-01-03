package com.quiz.javaquizapi.integration.facade.me;

import com.quiz.javaquizapi.integration.ApiIntegrationTests;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.Tiers;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public abstract class ProfileTests extends ApiIntegrationTests {
    @Getter(AccessLevel.PROTECTED)
    private final Profile localProfile = new Profile()
            .setScore(123L)
            .setRate(12000L)
            .setTier(Tiers.MIDDLE)
            .setUser(localUser);

    {
        localProfile.setCode(UUID.randomUUID().toString());
    }

    @BeforeEach
    protected void setUp() {
        initLog();
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}
