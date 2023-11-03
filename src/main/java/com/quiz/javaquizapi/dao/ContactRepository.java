package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.personal.Contact;

import java.util.Optional;

/**
 * Accumulates all functionality of <strong>the user contacts</strong> in the database.
 */
public interface ContactRepository extends BaseRepository<Contact> {
    /**
     * Finds a contact by an info code.
     *
     * @param personalInfoCode info code field value.
     * @return {@link Contact} if it exists. Optional field.
     */
    Optional<Contact> findByInfoCode(String personalInfoCode);

    /**
     * Checks if the <strong>Contact</strong> with an accepted info code exists.
     *
     * @param personalInfoCode info code field value.
     * @return <strong>true</strong> if a contact was found, <strong>false</strong> otherwise.
     */
    boolean existsByInfoCode(String personalInfoCode);
}
