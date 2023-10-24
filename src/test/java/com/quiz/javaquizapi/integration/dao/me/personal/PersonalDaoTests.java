package com.quiz.javaquizapi.integration.dao.me.personal;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.dao.PersonalInfoRepository;
import com.quiz.javaquizapi.dao.ProfileRepository;
import com.quiz.javaquizapi.integration.dao.MeDaoTests;
import com.quiz.javaquizapi.model.profile.Profile;
import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Getter(AccessLevel.PROTECTED)
public abstract class PersonalDaoTests<R extends BaseRepository> extends MeDaoTests<R> {
    protected static final String PROFILE_CODE = UUID.randomUUID().toString();
    protected static final String INFO_CODE = UUID.randomUUID().toString();

    @Autowired
    private PersonalInfoRepository infoRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private PersonalInfo testInfo;

    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = new Profile().setUser(localUser);
        testProfile.setCode(PROFILE_CODE);
        profileRepository.save(testProfile);

        testInfo = new PersonalInfo().setProfile(testProfile);
        testInfo.setCode(INFO_CODE);
        infoRepository.save(testInfo);
    }

}
