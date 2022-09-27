package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    Optional<Profile> findByUserUsername(String username);
}
