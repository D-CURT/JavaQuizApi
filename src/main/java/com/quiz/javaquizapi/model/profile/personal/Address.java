package com.quiz.javaquizapi.model.profile.personal;

import com.quiz.javaquizapi.model.BaseEntity;
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
@Table(name = "addresses")
@SequenceGenerator(name = "id_gen", sequenceName = "address_sec", allocationSize = 5)
public class Address extends BaseEntity implements Information<Address> {
    private String street;
    private String postalCode;
    private String city;
    private String region;
    private String country;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalInfo info;

    @Override
    public Address setInfo(PersonalInfo info) {
        this.info = info;
        return this;
    }
}
