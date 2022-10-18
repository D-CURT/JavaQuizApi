package com.quiz.javaquizapi.model.user;

import com.quiz.javaquizapi.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Contains a user details.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
@Accessors(chain = true)
@SequenceGenerator(name = "id_gen", sequenceName = "user_seq", allocationSize = 5)
public class User extends BaseEntity {

    @NotNull
    private String username;

    private String password;

    // TODO implement displayName inequality check
    private String displayName;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private Providers provider;

    private boolean enabled = true;
}
