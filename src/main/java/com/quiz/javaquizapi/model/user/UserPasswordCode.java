package com.quiz.javaquizapi.model.user;

import com.quiz.javaquizapi.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Table(name = "user_pass_codes")
@Accessors(chain = true)
@SequenceGenerator(name = "id_gen", sequenceName = "user_pass_seq", allocationSize = 5)
public class UserPasswordCode extends BaseEntity {

    private String username;
    private String passCode;
}
