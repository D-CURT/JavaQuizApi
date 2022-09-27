package com.quiz.javaquizapi.model.profile;

import com.quiz.javaquizapi.model.AbstractEntity;
import com.quiz.javaquizapi.model.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "profiles")
@SequenceGenerator(name = "id_gen", sequenceName = "profile_seq", allocationSize = 5)
public class Profile extends AbstractEntity {

    private Long score;

    @Enumerated(EnumType.STRING)
    private Tiers tier;

    @OneToOne
    private User user;
}
