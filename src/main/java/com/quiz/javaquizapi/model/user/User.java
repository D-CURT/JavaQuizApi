package com.quiz.javaquizapi.model.user;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.quiz.javaquizapi.model.AbstractEntity;

/**
 * Contains a user details.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
@Accessors(chain = true)
@SequenceGenerator(name = "id_gen", sequenceName = "user_seq", allocationSize = 5)
public class User extends AbstractEntity {

    @NotNull
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private Providers provider;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean enabled = true;
}
