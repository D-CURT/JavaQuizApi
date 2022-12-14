package com.quiz.javaquizapi.model.profile;

import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.model.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Contains a user personal details and the system statistics related to this profile.
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "profiles")
@SequenceGenerator(name = "id_gen", sequenceName = "profile_seq", allocationSize = 5)
public class Profile extends BaseEntity {

    /**
     * A user score. Scores can be used to accede to new knowledge.
     */
    private Long score;

    /**
     * A user self introduction.
     */
    private String info;

    @Enumerated(EnumType.STRING)
    private Tiers tier;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
