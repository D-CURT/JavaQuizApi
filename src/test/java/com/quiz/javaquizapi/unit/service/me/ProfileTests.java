package com.quiz.javaquizapi.unit.service.me;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.Tiers;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class ProfileTests extends ApiTests {
    @Getter(AccessLevel.PROTECTED)
    private final Profile localProfile = new Profile()
            .setScore(123L)
            .setRate(12_000L)
            .setTier(Tiers.MIDDLE)
            .setUser(localUser);

    {
        localProfile.setCode(UUID.randomUUID().toString());
    }

    @BeforeEach
    void setUp() {
        initLog();
    }

    @AfterEach
    void tearDown() {
        clearLog();
    }
}
