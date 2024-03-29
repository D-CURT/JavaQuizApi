package com.quiz.javaquizapi.model.user;

import com.quiz.javaquizapi.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * Contains a user details.
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@SequenceGenerator(name = "id_gen", sequenceName = "user_seq", allocationSize = 5)
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "UniqueNumberAndStatus", columnNames = {"username", "enabled"}))
public class User extends BaseEntity {
    @NotNull
    private String username;

    private String password;

    private String displayName;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private Providers provider;

    private Boolean enabled = Boolean.TRUE;
}
