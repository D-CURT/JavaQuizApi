package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.personal.PersonalInfo;

import java.util.Optional;

public interface PersonalInfoRepository extends BaseRepository<PersonalInfo> {

    Optional<PersonalInfo> findByProfileCode(String profileCode);

    boolean existsByProfileCode (String profileCode);
}
