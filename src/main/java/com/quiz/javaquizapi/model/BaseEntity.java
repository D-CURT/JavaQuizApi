package com.quiz.javaquizapi.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Contains common fields for all of the entities.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
    private Long id;

    /**
     * The common unique field in the <strong>UUID</strong> format.
     * <p>Use this field for database migrations and for the sake of entity search over the database.
     *
     * @see <a href="https://www.baeldung.com/java-uuid">UUID</a>
     */
    @NotNull
    private String code;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
