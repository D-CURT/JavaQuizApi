package com.quiz.javaquizapi.dao;

import com.quiz.javaquizapi.model.BaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Provides basic functionality of all repositories.
 *
 * @param <E> entity type extended the {@link BaseEntity}.
 */
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity> extends CrudRepository<E, Long> {

    Optional<E> findByCode(String code);

    boolean existsByCode(String code);
}
