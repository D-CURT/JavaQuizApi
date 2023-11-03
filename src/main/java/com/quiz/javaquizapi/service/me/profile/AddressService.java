package com.quiz.javaquizapi.service.me.profile;

import com.quiz.javaquizapi.model.profile.personal.Address;
import com.quiz.javaquizapi.service.QuizService;

import java.util.List;

/**
 * Provides functionality to operate with a user addresses objects {@link Address}.
 */
public interface AddressService extends QuizService<Address> {
    List<Address> getByPersonalInfoCode(String code);
}
