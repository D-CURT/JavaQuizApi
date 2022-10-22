package com.quiz.javaquizapi.service.security;

import com.quiz.javaquizapi.dao.UserRepository;
import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Provides functionality to integrate a user authorized by an external provider into the Quiz API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuizOAuth2LoginSuccessHandler implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof DefaultOidcUser oidcUser) {
            userRepository.findByUsername(oidcUser.getEmail())
                    .ifPresentOrElse(
                            user -> log.debug("Username '{}' already exists", user.getUsername()),
                            () -> {
                                User user = new User()
                                        .setUsername(oidcUser.getEmail())
                                        .setDisplayName(oidcUser.getAttribute("name"))
                                        .setRole(Roles.USER)
                                        .setEnabled(Boolean.TRUE)
                                        .setProvider(Providers.GOOGLE);
                                user.setCode(UUID.randomUUID().toString());
                                userRepository.save(user);
                                log.info("A new user created");
                                log.debug("Username '{}' resolved", oidcUser.getEmail());
                            });
        }
    }
}
