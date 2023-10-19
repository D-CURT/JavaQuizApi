package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.personal.Contact;

import java.util.Optional;

public interface ContactRepository extends BaseRepository<Contact>{

    Optional<Contact> findByInfoCode(String personalInfoCode);
}
