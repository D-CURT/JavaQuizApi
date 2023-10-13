package com.quiz.javaquizapi.model.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class AuthorizedUser extends User {

    private UUID id;
    private String displayName;

    public AuthorizedUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthorizedUser(
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public static AuthorizedUserBuilder builder(String username, String password,
                                                Collection<? extends GrantedAuthority> authorities) {
        return AuthorizedUserBuilder.configure()
                .username(username)
                .password(password)
                .authorities(authorities)
                .confirm();
    }

    public static AuthorizedUserBuilder builder(
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        return AuthorizedUserBuilder.configure()
                .username(username)
                .password(password)
                .enabled(enabled)
                .accountNonExpired(accountNonExpired)
                .credentialsNonExpired(credentialsNonExpired)
                .accountNonLocked(accountNonLocked)
                .authorities(authorities)
                .confirm();
    }

    @Builder(builderMethodName = "configure", buildMethodName = "confirm")
    private static class AuthorizedUserBuilder {
        private UUID id;
        private String displayName;
        String username;
        String password;
        boolean enabled;
        boolean accountNonExpired;
        boolean credentialsNonExpired;
        boolean accountNonLocked;
        Collection<? extends GrantedAuthority> authorities;
    }
}
