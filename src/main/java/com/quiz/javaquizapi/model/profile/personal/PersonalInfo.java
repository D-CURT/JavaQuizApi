package com.quiz.javaquizapi.model.profile.personal;

import com.quiz.javaquizapi.model.BaseEntity;
import com.quiz.javaquizapi.model.profile.Profile;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "personal_infos")
@SequenceGenerator(name = "id_gen", sequenceName = "personal_sec", allocationSize = 5)
public class PersonalInfo extends BaseEntity {

    private String bio;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private Profile profile;
}
