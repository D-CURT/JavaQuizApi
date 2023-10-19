package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.personal.SocialMedia;

import java.util.List;
import java.util.Optional;

public interface SocialMediaRepository extends BaseRepository<SocialMedia> {

    Optional<List<SocialMedia>> findByContactCode(String contactCode);
}
