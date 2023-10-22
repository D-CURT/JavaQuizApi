package com.quiz.javaquizapi.service.me.profile;

import com.quiz.javaquizapi.model.profile.personal.SocialMedia;
import com.quiz.javaquizapi.model.profile.personal.SocialType;
import com.quiz.javaquizapi.service.QuizService;

import java.util.List;

/**
 * Provides functionality to operate with a user social media objects {@link SocialMedia}.
 */
public interface SocialMediaService extends QuizService<SocialMedia> {

    List<SocialMedia> getByContactCode(String contactCode);

    boolean existByContactCodeAndType(String contactCode, SocialType type);
}
