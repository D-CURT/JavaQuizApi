package com.quiz.javaquizapi.model.user;

import lombok.Getter;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
public enum UserUpdateType {
    USERNAME(User::getUsername, User::setUsername),
    PASSWORD(User::getPassword, User::setPassword),
    DISPLAY_NAME(User::getDisplayName, User::setDisplayName);

    private final Function<User, String> getter;
    private final BiConsumer<User, String> setter;

    UserUpdateType(Function<User, String> getter, BiConsumer<User, String> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public void set(String value, User user) {
        setter.accept(user, value);
    }

    public boolean isPassword() {
        return UserUpdateType.PASSWORD.equals(this);
    }
}
