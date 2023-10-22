package com.quiz.javaquizapi.service.me.profile;

import com.quiz.javaquizapi.model.profile.personal.Contact;
import com.quiz.javaquizapi.service.QuizService;

/**
 * Provides functionality to operate with a user contacts {@link Contact}.
 */
public interface ContactService extends QuizService<Contact> {

    Contact getByPersonalInfoCode(String code);

    boolean existsByPersonalInfoCode(String code);
}
