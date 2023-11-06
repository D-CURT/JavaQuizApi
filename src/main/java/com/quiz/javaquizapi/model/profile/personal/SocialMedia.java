package com.quiz.javaquizapi.model.profile.personal;

import com.quiz.javaquizapi.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "social_medias")
@SequenceGenerator(name = "id_gen", sequenceName = "social_sec", allocationSize = 5)
public class SocialMedia extends BaseEntity {
    private String accountName;
    private String link;

    @Enumerated(EnumType.STRING)
    private SocialType type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Contact contact;
}
