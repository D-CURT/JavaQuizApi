package com.quiz.javaquizapi.model.profile.personal;

import com.quiz.javaquizapi.model.BaseEntity;

public interface Information<E extends BaseEntity> {
    E setInfo(PersonalInfo info);
}
