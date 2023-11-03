package com.quiz.javaquizapi.model.profile;

import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.model.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
     * A user personal rate on the platform. A user reputation.
     */
    private Long rate;

    @Enumerated(EnumType.STRING)
    private Tiers tier;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
