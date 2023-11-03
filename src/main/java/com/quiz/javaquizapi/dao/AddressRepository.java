package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.profile.personal.Address;

import java.util.List;
import java.util.Optional;

/**
 * Accumulates all functionality of <strong>the user address</strong> in the database.
 */
public interface AddressRepository extends BaseRepository<Address>{
    /**
     * Finds an address by an info code.
     *
     * @param infoCode info code field value.
     * @return {@link Address} if it exists. Optional field.
     */
    Optional<List<Address>> findByInfoCode(String infoCode);
}
