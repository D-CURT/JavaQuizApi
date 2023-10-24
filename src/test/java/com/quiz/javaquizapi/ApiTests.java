package com.quiz.javaquizapi;

import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;

@Getter(AccessLevel.PROTECTED)
public class ApiTests {
    private final OutputStream out = new ByteArrayOutputStream();
    private final OutputStream standardOut = System.out;

    protected final User localUser;

    {
        localUser = new User()
                .setUsername("username")
                .setPassword("password")
                .setDisplayName("displayName")
                .setRole(Roles.USER)
                .setProvider(Providers.LOCAL)
                .setEnabled(Boolean.TRUE);
        localUser.setCode(UUID.randomUUID().toString());
    }

    protected void initLog() {
        System.setOut(new PrintStream(out));
    }

    protected String captureLogs() {
        return out.toString();
    }

    protected void clearLog() {
        System.setOut(new PrintStream(standardOut));
    }
}
